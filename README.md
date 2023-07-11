# SimpleRestGetAPI

read-only API (REST) that returns one or more records from static set of job data.

Use case
 - Filter by one or more fields/attributes (e.g. /job_data?salary[gte]=120000) (Show only filtered row. Be able to filter all column depend on data type)
 - Filter by a sparse fields/attributes (e.g. /job_data?fields=job_title,gender,salary) (Show only filtered column)
 - Sort by one or more fields/attributes (e.g. /job_data?sort=job_title&sort_type=DESC)


http://localhost:8080/job_data?requestParam1=xxxx

RequestParameter:
- fields : comma-separated column name to filter column
- \<field name>[\<operator abbr.>] : filter row with field name and condition by operator abbr. If you don't specific[] mean equals(=), Add multiple criteria on the same field is available.
- sort : comma-separated column name to sort orderly, the column name in sort parameter need to be subset of column name in fields parameter
- sort_type : sorting direction [ASC, DESC]. ASC is default
 

operator abbr:
- lt : less than
- lte : less than or equals
- gt : greater than
- gte : greater than or equals
- eq : equals
- ne : not equals


Example:

show all column of filtered row that salary is equal 10000
*     /job_data?salary=10000

show all column of filtered row that salary is greater than or equal 10000
*     /job_data?salary[gte]=10000

show all column of filtered row that salary is equal 10000 and sort with Job Title and salary descendingly
*     /job_data?salary[eq]=10000&&sort=Job Title,salary&sort_type=DESC

show Timestamp,Additional Comments,Employer,salary columns of filtered row that salary is not equal 10000 and sort with salary and Employer ascendingly
*     /job_data?fields=Timestamp,Additional Comments,Employer,salary&salary[ne]=10000&sort=salary,Employer


show all column of filtered row that salary is not equal 2000 and less than 3000
*     /job_data?salary[ne]=2000&&salary[lt]=3000


Response body with data:
```
{
    errorCode: null,
    errorMsg: null,
    number_record: 2,
    data: [
              {"timestamp":"1", "salary": 123456},
              {"timestamp":"2", "salary": 789456}
         ]
}
```
Error Response body:
```
{
    errorCode: 123456,
    errorMsg: "Some Error",
    number_record: 0
}
```