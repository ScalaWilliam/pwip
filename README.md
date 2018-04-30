# PWIP
> The best Wiki ever

- Git
- WebSub
- JavaScript

# Usage
We use [SBT](https://www.scalawilliam.com/essential-sbt/).

## Development
```
$ sbt IntegrationTest/test
$ sbt run
```

## Standalone
```
$ sbt 'set version := "release"' 'show stage'
$ export PLAY_HTTP_SECRET_KEY=secret-key
$ ./target/universal/stage/bin/pwip
$ curl http://127.0.0.1:9000/ 
```

## Standalone (ZIP)
```
$ sbt 'set version := "release"' 'show dist'
$ mkdir -p target/test-environment
$ cd target/test-environment
$ unzip -o ../universal/pwip-release.zip
$ export PLAY_HTTP_SECRET_KEY=secret-key
$ ./pwip-release/bin/pwip
$ curl http://127.0.0.1:9000/
```

## Docker
```
$ sbt 'set dockerUpdateLatest := true' docker:publishLocal
$ docker run -e PLAY_HTTP_SECRET_KEY=secret-key -p 8080:9000 -it pwip
$ curl http://127.0.0.1:8080/ 
```

