package com.internet.sdk;

/*全局系统参数定义 （  sdk 包 内使用 可更改）
 * 作者： 徐川
 * 时间： 2016年3月2日
 * */
public class GlobalNames {
	public static String APPKEY = "8dc7959eeee2792ac2eebb490e60deed";
	// 当前使用的应用服务器类型(缺省 1) 1=weblogic;2=tomcat;3=jboos;4=websphere
	public static int APPLIATION_SERVERS = 1;
	// 日志文件名
	public static String LOG_FIlE_NAME = "logger%g.log";
	// 是否显示调试 sql 语句
	public static Boolean SHOW_SQL = true;
	// 是否显示警告
	public static Boolean SHOW_WARN = true;
	// 是否显示缓存日志
	public static Boolean SHOW_REDIS_LOG = true;
	// 本地数据库(业务数据) JNDI 名称
	public static String DB_JNDI_NAME = "";
	// 是否开启sql语句用时记录 false 关闭 true 打开
	public static boolean SQL_USE_TIME = true;
	// 是否开启 函数 用时记录
	public static boolean FUN_USE_TIME = true;
	// HBASE 端口
	public static String HBASE_PORT = "2181";
	// HBASE 服务器 IP
	public static String HBASE_SERVER_IP = "192.168.0.11,192.168.0.12,192.168.0.13";
	// 资源服务IP 地址 192.168.1.101
	public static String RESOURCE_SERVER_IP = "";
	// 资源服务端口地址
	public static String RESOURCE_SERVER_PORT = "";
	// 资源服务应用名称_
	public static String RESOURCE_SERVER_APPNAME = "";
	// 操作系统类型
	public static String OS_TYPE = "";

	// HBASE DAO 路径
	public static String HBASE_DAO_PATH = "";
	// db DAO 路径
	public static String DB_DAO_PATH = "";
	// 代码工程路径
	public static String CODE_SRC_PATH = "";
	// 运行时动态生成代码路径
	public static String CODE_SRC_PATH_RUN = "";
	// 代码创建模式 1 开始时创建，生成代码在 工程路径下， 不编译 。 2 运行时创建代码 ，生产代码在 发布路径下， 编译程序。
	public static String CODE_CREATE_MODEL = "1";

	// 当前连接的数据库名称
	public static String DATABASE_NAME = "";
	// 主键关键字前缀
	public final static String PRIMARY_KEY_PREFIX = "seq_";
	// 当前使用的数据库类型 1 oracle 2 Sqlserver 3 Mysql 4 sybase 5 Gbase
	public static int DATABASE_TYPE;

	// 记录生成主键序列的表名称
	public static String DATABASE_PRIMARY_TABLENAME = "pt_primary";
	// 记录生成主键序列的字段名称
	public static String DATABASE_PRIMARY_FILED = "intlsh";

	// 当前保存入库日期格式
	public static String DATA_FORMAT = "yyyy-MM-dd";
	public static String DATA_FORMAT_IFX = "MM/dd/yyyy";
	public static String DATA_FORMAT_TIME = "yyyy-MM-dd HH:mm:ss";

	// 是否启动时创建config.js(配置js文件)
	public static String CREATE_CONFIG_JS = "";
	// 配置config.js 路径
	public static String CONFIG_JS_PATH = "";
	// 应用id
	public static String APP_ID = "1";

	// redis 服务器集群
	public static String REDIS_CLUSTER = "";
	// redis 会话存储失效时间 30 分钟
	public static int REDIS_SESSION_TIME = 7200;
	// redis 密码
	//public static String REDIS_PASSWORD = "Zenith123";
	public static String REDIS_PASSWORD = "";

	// 上传文件缓存路径
	public final static String FILE_UPLOAD_CACHE_PATH = "c:\\TEMP";
	// 最大文件大小
	public final static int MAX_FILE_SIZE = 50000 * 1024; 
	// 最大内存大小
	public final static int MAX_MEM_SIZE = 50000 * 1024; 
	// 上传文件路径
	public static String FILE_UPLOAD_PATH = "fj";
	
	
	// 缓存失效时间 毫秒(30 分钟)
	public static long CACHE_SESSION_TIME = 30 * 60 * 60 * 1000; 
	// 统一单点登录失效时间
	public static int UNIFIED_SSO_FAILURE_TIME; 

	/**************************************[新增]***************************************/
	// 系统根路径
    public static String SERVER_ROOT="http://www.portal.com";
    // 系统端口
    public static String SERVER_PORT="";
    // 系统名称
    public static String SERVER_NAME="/portal";
	// RocketMQ NameServer
	public static String MQ_NAMESERVER = "192.168.3.6:9876;192.168.3.7:9876";
	// 表拥有者
	public static String DB_OWNER = ""; 
	// Session类型：Redis,Session
	public static String SESSION_TYPE = "Session";
	// 允许访问API的
	public static String ALLOW_APPKEYS = "8dc7959eeee2792ac2eebb490e60deed";
	// 调试状态(OFF 关闭  ON 开启）正式环境请关闭
	public static String DEBUG_MODE = "OFF";
	
	/**************************************[单点登录新增]***************************************/
	//是否启用单点登录 (true 启用 false 不启用)
    public static  String SSO_ISSSO="false"; 
    // 门户系统url
    public static String CITY_URL="";
    
    public static String COUNTY_URL="";
    
    public static String GENERAL_URL="";
    // 授权令牌失效时间为 （60 秒）
    public static long SSO_TOKEN_TIME=60 * 60 * 1000;
}