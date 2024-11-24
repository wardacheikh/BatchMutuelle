package org.example.batchassurance.Batch;

import org.example.batchassurance.DTO.MedicamentDto;
import org.example.batchassurance.Entity.Beneficiare;
import org.example.batchassurance.Entity.Dossier;
import org.example.batchassurance.Entity.Medicament;
import org.example.batchassurance.Logger.LoggingSkipPolicy;
import org.example.batchassurance.Logger.ValidationExceptionSkipListener;
import org.example.batchassurance.Processors.CalculProcessor;
import org.example.batchassurance.Processors.MedicamentProcessor;
import org.example.batchassurance.Processors.ValidationProcessor;
import org.example.batchassurance.Repository.BeneficireRepository;
import org.example.batchassurance.Repository.MedicamentReferentielRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
public class BatchConfig {


    private final BeneficireRepository beneficireRepository;
    private final MedicamentReferentielRepository medicamentReferentielRepository;

    private final PlatformTransactionManager transactionManager;

    private final JobRepository jobRepository;

    public BatchConfig(
            PlatformTransactionManager transactionManager, DataSource dataSource, BeneficireRepository beneficireRepository, MedicamentReferentielRepository medicamentReferentielRepository, JobRepository jobRepository) {

        this.transactionManager = transactionManager;
        this.beneficireRepository = beneficireRepository;
        this.medicamentReferentielRepository = medicamentReferentielRepository;

        this.jobRepository = jobRepository;
    }

    @Bean
    public ItemWriter<Beneficiare> dossierAssureItemWriter() {
        return new ItemWriter<Beneficiare>() {
            @Override
            public void write(Chunk<? extends Beneficiare> chunk) throws Exception {
                chunk.forEach(beneficireRepository::save);

            }

        };
    }

    @Bean
    public JsonItemReader<Dossier> dossierAssureItemReader() {
        return new JsonItemReaderBuilder<Dossier>()
                .jsonObjectReader(new JacksonJsonObjectReader<>(Dossier.class))
                .resource(new ClassPathResource("dossiers.json"))
                .name("dossierAssureItemReader")
                .build();
    }

    @Bean
    public CalculProcessor calculProcessor()
    {

        return  new CalculProcessor();
    }
    @Bean
    public ValidationProcessor validationProcessor()
    {

        return new ValidationProcessor();
    }

    @Bean
    public CompositeItemProcessor<Dossier, Beneficiare> compositeItemProcessor(
            ValidationProcessor validationProcessor,
            CalculProcessor calculProcessor) {

        CompositeItemProcessor<Dossier, Beneficiare> processor = new CompositeItemProcessor<>();
        processor.setDelegates(Arrays.asList(validationProcessor, calculProcessor));
        return processor;
    }

    @Bean
    public Step step1() {
        return new StepBuilder("step1", jobRepository)
                .<Dossier, Beneficiare>chunk(4, transactionManager)
                .reader(dossierAssureItemReader())
                .processor(compositeItemProcessor(validationProcessor(),calculProcessor()))
                .writer(dossierAssureItemWriter())
                .faultTolerant()
                .skip(ValidationException.class)
                .skipLimit(10)
                .listener(validationExceptionSkipListener())
                .build();
    }

    @Bean
    public Job job()
    {
        return new JobBuilder("job",jobRepository)
                .start(step1())
                .build();
    }


    @Bean
    public SkipPolicy loggingSkipPolicy() {
        return new LoggingSkipPolicy();
    }
    @Bean
    public ValidationExceptionSkipListener validationExceptionSkipListener() {
        return new ValidationExceptionSkipListener();
    }


    @Bean
    public FlatFileItemReader<MedicamentDto> medicamentCsvItemReader() {
        Resource resource = new ClassPathResource ("medicaments.csv");
        FlatFileItemReader<MedicamentDto> reader = new FlatFileItemReader<>();

        reader.setResource(resource);
        reader.setLinesToSkip(1);
        reader.setLineMapper(medicamentLineMapper());
        return reader;
    }

    @Bean
    public LineMapper<MedicamentDto> medicamentLineMapper() {
        DefaultLineMapper<MedicamentDto> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(","); // Délimiteur CSV
        tokenizer.setNames("code", "nom", "dci1", "dosage1", "uniteDosage1", "forme", "presentation",
                "ppv", "ph", "prixBr", "princepsGenerique", "tauxRemboursement");

        BeanWrapperFieldSetMapper<MedicamentDto> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(MedicamentDto.class);

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }


    @Bean
    public MedicamentProcessor medicamentProcessor()
    {
        return new MedicamentProcessor();
    }


    @Bean
    public ItemWriter<Medicament> medicamentItemWriter()
    {
        return new ItemWriter<Medicament>() {
            @Override
            public void write(Chunk<? extends Medicament> chunk) throws Exception {

                chunk.forEach(medicamentReferentielRepository::save);

            }
        };

    }


    @Bean
    public Step MedicamenStep()
    {
        return new StepBuilder("MdStep",jobRepository)
                .<MedicamentDto,Medicament>chunk(4,transactionManager)
                .reader(medicamentCsvItemReader())
                .processor(medicamentProcessor())
                .writer(medicamentItemWriter())
                .build();
    }

    @Bean
    public Job MdJob()
    {
        return new JobBuilder("MedicamentJob",jobRepository)
                .start(MedicamenStep())
                .build();
    }




}
