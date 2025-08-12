package com.hana7.hanaro.config;

import com.hana7.hanaro.entity.Orders;
import com.hana7.hanaro.repository.OrdersRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

	@Bean
	public Job csvJob(JobRepository jobRepository, Step step) {
		return new JobBuilder("csvJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(step)
			.build();
	}
	@Bean
	public Step step(JobRepository jobRepository,
		PlatformTransactionManager transactionManager, OrdersRepository ordersRepository) {
		return new StepBuilder("csvStep", jobRepository)
			.<Orders, Orders>chunk(5, transactionManager)
			.reader(orderReader()) // StepScope로 CSV 경로 받음
			.writer(orderWriter(ordersRepository))
			.build();
	}

	@Bean @StepScope
	protected FlatFileItemReader<Orders> orderReader() {
		return new FlatFileItemReaderBuilder<Orders>()
			.name("orderReader")
			.resource(new ClassPathResource("orders.csv"))
			.linesToSkip(1)
			.delimited()
			.names("totalPrice", "state")
			.fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
				setTargetType(Orders.class);
			}}).build();
	}

	@Bean
	public RepositoryItemWriter<Orders> orderWriter(CrudRepository<Orders, Long> repository) {
		return new RepositoryItemWriterBuilder<Orders>()
			.repository(repository)
			.methodName("save")
			.build();
	}
}
