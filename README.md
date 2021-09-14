# DeNA 2021 オータムハッカソン

## 概要

本プロジェクトは、ユーザが考案した旅行プランをシェアするサービスです。

オリジナルの旅行プランをシェアしよう！

## 開発

### 開発環境

- Java OpenJDK 11
- Spring Boot
- MySQL
- docker-compose

### ビルド方法

ビルドに成功すると、`app/bulid/libs`直下に`.jar`ファイルが生成されます。

```sh
$ ./gradlew build -x test
```

### 起動方法

デフォルトで使用されるポート番号は`8080`です。`-Dserver.port=XXXX`オプションを付けることでポート番号を変更できます。

```sh
1. run .jar file
$ java -jar <app name>-<version>.jar  # -Dspring.profiles.active=<environment>

2. run on dev environment
$ ./gradlew bootRun
```

### API ドキュメント

本プロジェクトは OpenAPI を採用しています。ドキュメントを確認する場合は下記に従ってください。

1. アプリケーションを起動
2. [Swagger UI](http://localhost:8080/swagger-ui/)へアクセス
