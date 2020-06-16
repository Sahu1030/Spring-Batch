//package com.sarat.springbatch.batchconfig;
//
//
//	import java.util.LinkedList;
//	import java.util.List;
//	import java.util.Random;
//
//	import org.springframework.batch.core.ExitStatus;
//	import org.springframework.batch.core.Job;
//	import org.springframework.batch.core.Step;
//	import org.springframework.batch.core.StepExecution;
//	import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//	import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//	import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//	import org.springframework.batch.core.configuration.annotation.StepScope;
//	import org.springframework.batch.core.launch.support.RunIdIncrementer;
//	import org.springframework.batch.core.listener.StepExecutionListenerSupport;
//	import org.springframework.batch.item.ItemWriter;
//	import org.springframework.batch.item.support.ListItemReader;
//	import org.springframework.batch.repeat.RepeatStatus;
//	import org.springframework.context.annotation.Bean;
//	import org.springframework.context.annotation.Configuration;
//
//	/**
//	 * @author Michael Minella
//	 * @author Mahmoud Ben Hassine
//	 */
//	@Configuration
//	@EnableBatchProcessing
//	public class MultiBatchConfigure {
//
//		private Random random;
//		private JobBuilderFactory jobBuilderFactory;
//		private StepBuilderFactory stepBuilderFactory;
//
//		public MultiBatchConfigure(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
//			this.jobBuilderFactory = jobBuilderFactory;
//			this.stepBuilderFactory = stepBuilderFactory;
//			this.random = new Random();
//		}
//
//		@Bean
//		public Job job() {
//			return jobBuilderFactory.get("job")
//					.start(step1())
//					.next(step2())
//					.incrementer(new RunIdIncrementer())
//					.build();
//		}
//
//		@Bean
//		public Step step1() {
//			return stepBuilderFactory.get("step1")
//					.<Integer, Integer>chunk(3)
//					.reader(itemReader())
//					.writer(itemWriter())
//					.listener(new StepExecutionListenerSupport() {
//						@Override
//						public ExitStatus afterStep(StepExecution stepExecution) {
//							stepExecution.getJobExecution().getExecutionContext()
//									.putInt("readCount", stepExecution.getReadCount());
//							return super.afterStep(stepExecution);
//						}
//					})
//					.build();
//		}
//
//		@Bean
//		public Step step2() {
//			return stepBuilderFactory.get("step2")
//					.tasklet((stepContribution, chunkContext) -> {
//						int readCount = chunkContext.getStepContext().getStepExecution()
//								.getJobExecution().getExecutionContext()
//								.getInt("readCount");
//						System.out.println("readCount = " + readCount);
//						return RepeatStatus.FINISHED;
//					})
//					.build();
//		}
//
//		@Bean
//		@StepScope
//		public ListItemReader<Integer> itemReader() {
//			List<Integer> items = new LinkedList<>();
//			// read a random number of items in each run
//			for (int i = 0; i < random.nextInt(100); i++) {
//				items.add(i);
//			}
//			return new ListItemReader<>(items);
//		}
//
//		@Bean
//		public ItemWriter<Integer> itemWriter() {
//			return items -> {
//				for (Integer item : items) {
//					int nextInt = random.nextInt(1000);
//					Thread.sleep(nextInt);
//					// simulate write failure
//					if (nextInt % 57 == 0) {
//						throw new Exception("Boom!");
//					}
//					System.out.println("item = " + item);
//				}
//			};
//		}
//
//	}
