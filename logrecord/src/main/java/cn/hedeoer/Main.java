package cn.hedeoer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {

/*        logger.trace("This is a TRACE message");
        logger.info("This is an INFO message");
        logger.debug("This is a DEBUG message");*/
        logger.warn("This is a WARN message");
        logger.error("This is an ERROR message");
    }
}