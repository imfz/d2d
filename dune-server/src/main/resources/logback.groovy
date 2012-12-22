import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender

import static ch.qos.logback.classic.Level.DEBUG
import static ch.qos.logback.classic.Level.ERROR
import static ch.qos.logback.classic.Level.INFO

appender("STDOUT", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%date{dd MMM yyyy HH:mm:ss.SSS} %-7relative [%-20thread] %-5level %logger - %msg %n"
    }
}
root(INFO, ["STDOUT"])
logger("org.hibernate.SQL", DEBUG);
logger("org.apache.commons.httpclient.HttpMethodBase", ERROR);