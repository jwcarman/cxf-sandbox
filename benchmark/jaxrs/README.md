# JAX-RS Benchmarking

## Starting the Server

From the "server" module, run:
 
```
mvn -Pasync exec:java
```

or 

```
mvn -Psync exec:java
```

where the profile name indicates which type of JAX-RS server you wish to stary (asynchronous vs. synchronous)

### Running Gatling

From the "gatling" module, run:

```
mvn clean install gatling:exec
```

You can also customize the gatling job:

```
mvn clean install gatling:exec -Dusers=200 -Drepeat=100 -Dduration=10
```

where the options are:

Option   | Default | Description 
---------|---------|-------------------------------------
users    | 500     | The number of users
duration | 10      | The user ramp-up duration (in seconds)
repeat   | 1000    | The number of requests per user



# Example Output

```
================================================================================
---- Global Information --------------------------------------------------------
> request count                                     500000 (OK=500000 KO=0     )
> min response time                                      0 (OK=0      KO=-     )
> max response time                                    212 (OK=212    KO=-     )
> mean response time                                    66 (OK=66     KO=-     )
> std deviation                                         24 (OK=24     KO=-     )
> response time 50th percentile                         59 (OK=59     KO=-     )
> response time 75th percentile                         75 (OK=75     KO=-     )
> mean requests/sec                                6675.033 (OK=6675.033 KO=-  )
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                        500000 (100%)
> 800 ms < t < 1200 ms                                   0 (  0%)
> t > 1200 ms                                            0 (  0%)
> failed                                                 0 (  0%)
================================================================================
```


```
================================================================================
---- Global Information --------------------------------------------------------
> request count                                     500000 (OK=500000 KO=0     )
> min response time                                      0 (OK=0      KO=-     )
> max response time                                    137 (OK=137    KO=-     )
> mean response time                                     9 (OK=9      KO=-     )
> std deviation                                          6 (OK=6      KO=-     )
> response time 50th percentile                          9 (OK=10     KO=-     )
> response time 75th percentile                         11 (OK=11     KO=-     )
> mean requests/sec                                8261.732 (OK=8261.732 KO=-     )
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                        500000 (100%)
> 800 ms < t < 1200 ms                                   0 (  0%)
> t > 1200 ms                                            0 (  0%)
> failed                                                 0 (  0%)
================================================================================
```

```
================================================================================
---- Global Information --------------------------------------------------------
> request count                                     500000 (OK=500000 KO=0     )
> min response time                                      0 (OK=0      KO=-     )
> max response time                                    253 (OK=253    KO=-     )
> mean response time                                     9 (OK=9      KO=-     )
> std deviation                                          5 (OK=5      KO=-     )
> response time 50th percentile                          8 (OK=8      KO=-     )
> response time 75th percentile                         11 (OK=11     KO=-     )
> mean requests/sec                                8868.865 (OK=8868.865 KO=-     )
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                        500000 (100%)
> 800 ms < t < 1200 ms                                   0 (  0%)
> t > 1200 ms                                            0 (  0%)
> failed                                                 0 (  0%)
================================================================================
```
