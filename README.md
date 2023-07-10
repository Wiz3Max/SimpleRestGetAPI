# SimpleRestGetAPI

read-only API (REST) that returns one or more records from static set of job data.

Use case
 - Filter by one or more fields/attributes (e.g. /job_data?salary[gte]=120000) (Show only filtered row. Expected filter able column: job title, salary, gender )
 - Filter by a sparse fields/attributes (e.g. /job_data?fields=job_title,gender,salary) (Show only filtered column)
 - Sort by one or more fields/attributes (e.g. /job_data?sort=job_title&sort_type=DESC)


/job_data?requestParam1=xxxx

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
*      /job_data?salary=10000  #filter row that salary equal 10000
*      /job_data?salary[gte]=10000  #filter row that salary greater than or equal 10000
*      /job_data?salary=10000&&sort=name,salary&&sort_type=DESC

Response body with data:
```
{
    errorCode: 000,
    errorMsg: "",
    number_record: 123,
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