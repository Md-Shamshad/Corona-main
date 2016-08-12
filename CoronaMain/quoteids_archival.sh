#--------------------------------INVOKE MASTER TABLE ARCHIVAL PROCEDURE----------------------------------------------
HOME_DIR=/opt_apps/corona/loads/archival/

CL=$HOME_DIR/classes
CL=$CL:$HOME_DIR/conf
CL=$CL:.$CLASSPATH
CL=$CL:$HOME_DIR/lib/activation.jar
CL=$CL:$HOME_DIR/lib/classes12.jar
CL=$CL:$HOME_DIR/lib/commons-beanutils.jar
CL=$CL:$HOME_DIR/lib/commons-collections.jar
CL=$CL:$HOME_DIR/lib/commons-dbcp-1.2.1.jar
CL=$CL:$HOME_DIR/lib/commons-digester.jar
CL=$CL:$HOME_DIR/lib/commons-discovery.jar
CL=$CL:$HOME_DIR/lib/commons-io-1.4.jar
CL=$CL:$HOME_DIR/lib/commons-lang-2.4.jar
CL=$CL:$HOME_DIR/lib/commons-logging.jar
CL=$CL:$HOME_DIR/lib/NetComponents.jar
CL=$CL:$HOME_DIR/lib/commons-logging-1.1.jar
CL=$CL:$HOME_DIR/lib/commons-pool-1.3.jar
CL=$CL:$HOME_DIR/lib/commons-validator.jar
CL=$CL:$HOME_DIR/lib/crimson.jar
CL=$CL:$HOME_DIR/lib/dom4j-full.jar
CL=$CL:$HOME_DIR/lib/expresso-v4.0-e3.0.jar
CL=$CL:$HOME_DIR/lib/jakarta-oro-2.0.6.jar
CL=$CL:$HOME_DIR/lib/jakarta-regexp-1.2.jar
CL=$CL:$HOME_DIR/lib/java-getopt-1.0.9.jar
CL=$CL:$HOME_DIR/lib/jaxrpc.jar
CL=$CL:$HOME_DIR/lib/jcert.jar
CL=$CL:$HOME_DIR/lib/jdom-1.0.jar
CL=$CL:$HOME_DIR/lib/log4j-1.2.4.jar
CL=$CL:$HOME_DIR/lib/ojdbc14.jar
CL=$CL:$HOME_DIR/lib/CoronaCommon.jar
CL=$CL:$HOME_DIR/lib/jep-2.24.jar
CL=$CL:$HOME_DIR/lib/jsch-0.1.37.jar
CL=$CL:$HOME_DIR/lib/jsr173_1.0_api.jar
CL=$CL:$HOME_DIR/lib/jxl.jar
CL=$CL:$HOME_DIR/lib/libsapjco3.so
CL=$CL:$HOME_DIR/lib/log4j-1.2.4.jar
CL=$CL:$HOME_DIR/lib/mail.jar
CL=$CL:$HOME_DIR/lib/sapjco3.jar
CL=$CL:$HOME_DIR/lib/schemas.jar
CL=$CL:$HOME_DIR/lib/servlet.jar
CL=$CL:$HOME_DIR/lib/struts.jar
CL=$CL:$HOME_DIR/lib/xalan.jar
CL=$CL:$HOME_DIR/lib/xbean.jar
CL=$CL:$HOME_DIR/lib/xbean_xpath.jar
CL=$CL:$HOME_DIR/lib/xerces.jar
CL=$CL:$HOME_DIR/lib/xml-apis.jar
CL=$CL:$HOME_DIR/lib/wlthint3client.jar
CL=$CL:$HOME_DIR/lib/commons-httpclient-3.1.jar
CL=$CL:$HOME_DIR/lib/org.apache.commons.codec.jar

cd $HOME_DIR/scripts

CLASSPATH=$CL

#Argment has to be passed to java prgram, if archive_frequency is one of the following then pass appropriate value to java programe
#IF JOB_INFO.JI_ARCHIVE_FREQUENCY == DAILY --> THEN PASS 1
#IF JOB_INFO.JI_ARCHIVE_FREQUENCY == WEEKLY --> THEN PASS 7
#IF JOB_INFO.JI_ARCHIVE_FREQUENCY == MONTHLY --> THEN PASS 30

java -Dcomcat.config=/opt_apps/corona/loads/archival/conf/comcat-config.xml  -Dcomcat.config.dir=/opt_apps/corona/loads/archival/conf   -Dcomcat.home=/opt_apps/corona/loads/archival/  -Dcomcat.ctx=com.hp.psg.corona.ctx.util.BeCTX -cp $CLASSPATH com.hp.psg.corona.archival.handler.quote.QuoteArchivalProcessor


#--------------------------------INVOKE DC TABLE PURGE PROCEDURE----------------------------------------------
#invoking DC table purge procedure
#!/bin/sh
DB_IND=QUOTE_IDS_ITG; export DB_IND
export ORACLE_SID=`cat /opt_apps/corona/database/corona_dbparam | grep $DB_IND | awk -F: '{print $5}'`
export ORACLE_HOME=`cat /opt_apps/corona/database/corona_dbparam | grep $DB_IND | awk -F: '{print $4}'`
UNAME=`cat /opt_apps/corona/database/corona_dbparam | grep $DB_IND | awk -F: '{print $2}'`
PWORD=`cat /opt_apps/corona/database/corona_dbparam | grep $DB_IND | awk -F: '{print $3}'`

export ORACLE_BIN=$ORACLE_HOME/bin
export PATH=$ORACLE_HOME/bin:$PATH

HOME_DIR='/opt_apps/corona/loads/archival'
LOG_DIR=$HOME_DIR'/scripts/LOG_DIR/'
SQL_DIR=$HOME_DIR'/sql'
LOG_DATE=`date +'%Y%m%d-%H%M'`
FILE_NAME='archival'
LOG_FILE="$FILE_NAME"_$LOG_DATE.log

cd $SQL_DIR

echo Start time: `date +'%Y%m%d-%H%M%S'`  > $LOG_DIR$LOG_FILE
sqlplus -s $UNAME/$PWORD@$ORACLE_SID  < $SQL_DIR/quoteids_archival.sql  >> $LOG_DIR$LOG_FILE

if [[ $? -ne 0 ]]; then

echo "Archival load failed "  >> $LOG_DIR$LOG_FILE

exit 1

else

echo End time: `date +'%Y%m%d-%H%M%S'`  >> $LOG_DIR$LOG_FILE

exit 0

fi