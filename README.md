# scalaWithTextandElasticSearch
Learning to use Scala by ingesting text file(s) into ElasticSearch.

Read the full blog here https://www.confessionsofadataguy.com/scala-with-text-files-and-elasticsearch/

You will need a local Docker instance of elasticsearch running... easy as ....

`docker run -d -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:7.9.0`


