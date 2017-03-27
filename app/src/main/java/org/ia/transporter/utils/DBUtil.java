package org.ia.transporter.utils;

import android.os.Environment;

import org.xutils.DbManager;
import org.xutils.x;

import java.io.File;

/**
 * Created by Administrator on 2017/2/11 0011.
 */

public class DBUtil {

    public static DbManager db;

    public static void initDB() {
        File file = new File(Environment.getExternalStorageDirectory().getPath());          //安卓根目录
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("ic_launcher.db")    //创建数据库的名称
                .setDbVersion(1)                //数据库版本号
                .setDbDir(file)                 //设置数据库db文件路径
                .setAllowTransaction(true)      //开启事务
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        db.getDatabase().enableWriteAheadLogging();
                        //开启WAL, 对写入加速提升巨大(作者原话)
                    }
//                })
//                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
//                    @Override
//                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
//                        // TODO: ...
//                    }
                });     //数据库更新操作
        db = x.getDb(daoConfig);
    }

}
