# RealtimeChatApp v2

Minor updates since previous revision:

* Default port **6500** (was 6000)
* Timestamp added to all broadcast messages
* Client prompts for **nickname**
* Window title now "Realtime Chat App v2"
* Compile with Java 17

## Build

```bash
mvn clean package
```

## Run

```bash
java -cp target/classes com.example.chat.RealTimeChatServer
java -cp target/classes com.example.chat.RealTimeChatClient
```
