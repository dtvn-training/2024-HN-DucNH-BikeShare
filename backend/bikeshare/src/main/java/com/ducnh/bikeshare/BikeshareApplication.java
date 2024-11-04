package com.ducnh.bikeshare;

import com.google.cloud.bigquery.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BikeshareApplication {

	public static void main(String[] args) {
		SpringApplication.run(BikeshareApplication.class, args);
//		String datasetName = "test";
//		String tableName = "test";
//		String query =
//				"SELECT *"
//						+ " FROM `springboot-bq.bikeshare.stations`";
//		queryPagination(datasetName, tableName, query);
	}

//	public static void queryPagination(String datasetName, String tableName, String query) {
//		try {
//			// Initialize client that will be used to send requests. This client only needs to be created
//			// once, and can be reused for multiple requests.
//			BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();
//
//			TableId tableId = TableId.of(datasetName, tableName);
//			QueryJobConfiguration queryConfig =
//					QueryJobConfiguration.newBuilder(query)
//							// save results into a table.
//							.setDestinationTable(tableId)
//							.build();
//
//			bigquery.query(queryConfig);
//
//			TableResult results =
//					bigquery.listTableData(tableId, BigQuery.TableDataListOption.pageSize(10));
//
//			int pageCount=0;
//			int rowCount=0;
//			do {
//				System.out.println("page number: " + (++pageCount));
//				for (FieldValueList row : results.getValues()) {
//					rowCount++;
//				}
//				System.out.println("Fetched " + rowCount+ " records");
//				results = results.getNextPage();
//				rowCount = 0;
//			} while (results.hasNextPage());
//
//			//for the last page
//			System.out.println("page number: " + (++pageCount));
//			for (FieldValueList row : results.getValues()) {
//				rowCount++;
//			}
//			System.out.println("Fetched " + rowCount + " records");
//
//			System.out.println("Query ran successfully");
//
//			while (results.hasNextPage()) {
//				// Remaining Pages
//				results = results.getNextPage();
//				results
//						.getValues()
//						.forEach(row -> row.forEach(val -> System.out.printf("%s,\n", val.toString())));
//			}
//
//			System.out.println("Query pagination performed successfully.");
//		} catch (BigQueryException | InterruptedException e) {
//			System.out.println("Query not performed \n" + e.toString());
//		}
//	}
}