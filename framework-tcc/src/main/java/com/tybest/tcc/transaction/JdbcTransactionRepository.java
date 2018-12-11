package com.tybest.tcc.transaction;

import com.tybest.tcc.exception.InternalException;
import com.tybest.tcc.serializer.JdkSerializer;
import com.tybest.tcc.serializer.Serializer;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

/**
 * @author tb
 * @date 2018/12/11 11:48
 */
@Getter
@Setter
public class JdbcTransactionRepository extends AbstractTransactionRepository {

    private static final String DEF_TABLE_NAME = "tcc_transaction";
    private String domain;
    private String tbSuffix;
    private DataSource dataSource;
    private Serializer serializer = new JdkSerializer();

    @Override
    protected int doCreate(Transaction transaction) {
        return 0;
    }

    @Override
    protected int doUpdate(Transaction transaction) {
        return 0;
    }

    @Override
    protected int doDelete(Transaction transaction) {
        return 0;
    }

    @Override
    protected Transaction doFindOne(TransactionXid xid) {
        return null;
    }

    @Override
    protected List<Transaction> doFindAllUnmodifiedSince(Date date) {
        return null;
    }

    private Connection getConnection(){
        try {
            return this.dataSource.getConnection();
        } catch (SQLException e) {
            throw new InternalException("get connection failed",e);
        }
    }

    private void closeStatement(Statement statement) {
        try {
            if (statement != null && !statement.isClosed()) {
                statement.close();
            }
        } catch (Exception ex) {
            throw new InternalException("close statement failed",ex);
        }
    }

    private void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new InternalException("close connectino failed",e);
        }
    }

    private String getSql(){
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ")
            .append(getTableName())
            .append("(global_tx_id,branch_qualifier,transaction_type,content,status,retries,create_time,last_update_time,version");
        if(StringUtils.isBlank(getDomain())){
            sb.append(") VALUES(?,?,?,?,?,?,?,?,?)");
        }else {
            sb.append(",domain) VALUES(?,?,?,?,?,?,?,?,?,?)");
        }
        return sb.toString();
    }

    private String getTableName(){
        return StringUtils.isBlank(tbSuffix)? DEF_TABLE_NAME: DEF_TABLE_NAME+tbSuffix;
    }
}
