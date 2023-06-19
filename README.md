# Search Sample Project

## Prerequisites
- Kotlin 1.8.21 / Java 17 / Gradle

## External Library
- Gson (for serializ & deserialize between JSON and kotlin objects)

## Installation
1. gradle build
2. java -jar ./build/libs/search-0.0.1-SNAPSHOT.jar

## APIs
### 1. GET: /search/blog
- request
```
GET /search/blog?query={}&page={}&size={}&sort={}
```
- respose
```json
{
    "content": [
        {
            "title": ...,
            "contents": ...,
            "url": ...,
            "blogname": ...,
            "thumbnail": ...,
            "datetime": ...
        },
      ...
    ],
    "pageable": {
        "sort": {
            "empty": ...,
            "unsorted": ...,
            "sorted": ...
        },
        "offset": ...,
        "pageNumber": ...,
        "pageSize": ...,
        "paged": ...,
        "unpaged": ...
    },
    "totalElements": ...,
    "totalPages": ...,
    "last": ...
}
```
- example request
```
curl -X GET 'http://localhost:8080/search/blog?query=test'
curl -X GET 'http://localhost:8080/search/blog?query=test&sort=accuracy&page=1&size=10'
```

<br/><br/><br/>

### 2. GET: /search/history
- request
```
GET /search/history
```
- respose
```json
{
    "keywords": [
        {
            "keyword": "asdf",
            "count": 2
        },
        {
            "keyword": "aaa",
            "count": 1
        },
        ...
    ]
}
```
- example request
```
curl -X GET 'localhost:8080/search/history'
```
