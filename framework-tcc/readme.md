### TCC(try-confirm-cancel)




### JTA
    ACID
#### 本地事务
    public void transferAccount() {
        Connection conn = null;
        Statement stmt = null;
        try{
            conn = getDataSource().getConnection();
            //若设置为 true 则数据库将会把每一次数据更新认定为一个事务并自动提交
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            // 将 A 账户中的金额减少 500
            stmt.execute("\
            update t_account set amount = amount - 500 where account_id = 'A'");
            // 将 B 账户中的金额增加 500
            stmt.execute("\
            update t_account set amount = amount + 500 where account_id = 'B'");
            // 提交事务
            conn.commit();
            // 事务提交：转账的两步操作同时成功
        } catch(SQLException sqle){
            try{
                // 发生异常，回滚在本事务中的操做
               conn.rollback();
                // 事务回滚：转账的两步操作完全撤销
                stmt.close();
                conn.close();
            }catch(Exception ignore){

            }
            sqle.printStackTrace();
        }
    }
#### 分布式事务
    JTA(Java Transaction API)  JTS(Java Transaction Service)
    事务管理器 ： 承担着所有事务参与单元的协调与控制
    一个获多个支持XA协议的资源管理器(Resource Manager) == 任意类型的持久化数据存储；

    JTA 事务有效的屏蔽了底层事务资源，使应用可以以 透明 的方式参入到事务处理中；
    但是与本地事务相比，XA 协议的系统开销大，在系统开发过程中应慎重考虑是否确实需要分布式事务。

    public void transferAccount() {
        UserTransaction userTx = null;
        Connection connA = null;
        Statement stmtA = null;
        Connection connB = null;
        Statement stmtB = null;
        try{
              // 获得 Transaction 管理对象
            userTx = (UserTransaction)getContext().lookup("\
                  java:comp/UserTransaction");
            // 从数据库 A 中取得数据库连接
            connA = getDataSourceA().getConnection();
            // 从数据库 B 中取得数据库连接
            connB = getDataSourceB().getConnection();
                       // 启动事务
            userTx.begin();

            // 将 A 账户中的金额减少 500
            stmtA = connA.createStatement();
            stmtA.execute("
           update t_account set amount = amount - 500 where account_id = 'A'");

            // 将 B 账户中的金额增加 500
            stmtB = connB.createStatement();
            stmtB.execute("\
            update t_account set amount = amount + 500 where account_id = 'B'");

            // 提交事务
            userTx.commit();
            // 事务提交：转账的两步操作同时成功（数据库 A 和数据库 B 中的数据被同时更新）
        } catch(SQLException sqle){
            try{
                  // 发生异常，回滚在本事务中的操纵
                 userTx.rollback();
                // 事务回滚：转账的两步操作完全撤销
                //( 数据库 A 和数据库 B 中的数据更新被同时撤销）
                stmt.close();
                conn.close();
                ...
            }catch(Exception ignore){
            }
            sqle.printStackTrace();

        } catch(Exception ne){
            e.printStackTrace();
        }
    }

