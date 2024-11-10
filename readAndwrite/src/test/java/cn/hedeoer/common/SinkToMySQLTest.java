package cn.hedeoer.common;

import static org.junit.jupiter.api.Assertions.*;

class SinkToMySQLTest {

    @org.junit.jupiter.api.Test
    void sink() {
        String data = "{ \"meta\": [ { \"name\": \"city\", \"type\": \"String\" }, { \"name\": \"temperature\", \"type\": \"Float64\" } ], \"data\": [ { \"city\": \"Abha\", \"temperature\": 27.1 }, { \"city\": \"Abha\", \"temperature\": 16.9 }, { \"city\": \"Abha\", \"temperature\": 25.39 }, { \"city\": \"Abha\", \"temperature\": 18.62 }, { \"city\": \"Abha\", \"temperature\": -1.04 }, { \"city\": \"Abha\", \"temperature\": 23.01 }, { \"city\": \"Abha\", \"temperature\": 2.82 }, { \"city\": \"Abha\", \"temperature\": 9.44 }, { \"city\": \"Abha\", \"temperature\": 21.93 }, { \"city\": \"Abha\", \"temperature\": 29.62 } ], \"rows\": 10, \"rows_before_limit_at_least\": 10, \"statistics\": { \"elapsed\": 0.004552388, \"rows_read\": 20, \"bytes_read\": 420 } }";

        SinkToMySQL sinkToMySQL = new SinkToMySQL();
        sinkToMySQL.sink(data,0);

    }

    @org.junit.jupiter.api.Test
    void getConnection() {
    }
}