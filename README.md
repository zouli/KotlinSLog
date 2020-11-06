# KotlinSLog

## SLog
### SLog.TAG_FORMAT
```
    /**
     * TAG格式
     * 模板字段格式为 {{关键字}}
     * 关键字：
     *      thread_name 线程名称
     *      thread_id   线程ID
     *      filename    执行文件名
     *      line_number 执行行数
     *      method_name 执行方法名
     *      now|format  当前时间，format使用SimpleDateFormat语法设置
     */
```
默认格式  `var TAG_FORMAT = "[{{thread_name}}({{thread_id}}):{{filename}}({{line_number}}):{{method_name}}]"`

## Printer
输出器需要实现Printer接口 

现提供输出器  
+ SLogPrinter *Logcat输出器*
+ FilePrinter *文件输出器*
+ NetPrinter  *网络接口输出器*

### FilePrinter.FILENAME_FORMAT
```
    /**
     * 文件名格式
     * 模板字段格式为 {{关键字}}
     * 关键字：
     *      now|format  当前时间，format使用SimpleDateFormat语法设置
     */
```
默认格式  `var FILENAME_FORMAT = "/{{now|yyyyMMdd}}.log"`
     
### FilePrinter.MSG_FORMAT
```
    /**
     * 信息格式
     * 模板字段格式为 {{关键字}}
     * 关键字：
     *      level_long  LOG基本名
     *      level_short LOG基本名（短）
     *      tag         TAG名
     *      msg         信息
     *      now|format  当前时间，format使用SimpleDateFormat语法设置
     */
```
默认格式  `var MSG_FORMAT = "{{level_short}}/{{tag}}: {{msg}}"`
