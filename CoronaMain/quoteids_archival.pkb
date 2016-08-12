CREATE OR REPLACE PACKAGE BODY QUOTE_IDS.ARCH_QUOTE_IDS
IS

PROCEDURE ARC_ON_QUOTE_MASTER_TABLES(o_status OUT VARCHAR2, o_msg OUT VARCHAR2)
AS

    Query_exec VARCHAR2(4000);

    TYPE objectnameTab IS TABLE OF ARCHIVAL_OBJECTS.AO_OBJECT_NAME%TYPE;
    objectname objectnameTab := objectnameTab();

    TYPE  graceperiodTab IS TABLE OF ARCHIVAL_OBJECTS.AO_GRACE_PERIOD%TYPE;
    graceperiod graceperiodTab := graceperiodTab();

    TYPE conditionTab IS TABLE OF ARCHIVAL_OBJECTS.AO_CONDITION%TYPE;
    conditions conditionTab := conditionTab();

    TYPE objecttypeTab IS TABLE OF ARCHIVAL_OBJECTS.AO_OBJECT_TYPE%TYPE;
    objecttype objecttypeTab := objecttypeTab();

    TYPE aoskipflagTab IS TABLE OF ARCHIVAL_OBJECTS.AO_SKIP_FLAG%TYPE;
    aoskipflag aoskipflagTab := aoskipflagTab();

    TYPE slsqtnidTab IS TABLE OF SLS_QTN_VRSN.SLS_QTN_ID%TYPE;
    slsqtnid slsqtnidTab := slsqtnidTab();

    TYPE slsqtnsqnnridTab IS TABLE OF SLS_QTN_VRSN.SLS_QTN_VRSN_SQN_NR%TYPE;
    slsqtnvrsnsqnnr slsqtnsqnnridTab := slsqtnsqnnridTab();


    V_LAST_RUN DATE;

    V_RUN_STAT VARCHAR2(1);

    V_ARCHIVE_FREQUENCY VARCHAR2(50);
    
    V_STATE_EXECUTE VARCHAR2(1);
    
    l_cnt number := 0;
    

BEGIN

    SELECT  JI_LAST_RUN_DATE , JI_LAST_RUN_STATUS, JI_ARCHIVE_FREQUENCY INTO V_LAST_RUN, V_RUN_STAT, V_ARCHIVE_FREQUENCY FROM JOB_INFO
              WHERE JI_NAME = 'ARC_ON_QUOTE_MASTER_TABLE' AND JI_TYPE = 'ARCHIVAL';              

    IF V_ARCHIVE_FREQUENCY = 'DAILY' AND V_LAST_RUN + 1  <= SYSDATE THEN
        V_STATE_EXECUTE := 'Y';
    ELSIF V_ARCHIVE_FREQUENCY = 'WEEKLY' AND V_LAST_RUN + 7  <= SYSDATE THEN
        V_STATE_EXECUTE := 'Y';
    ELSIF V_ARCHIVE_FREQUENCY = 'MONTHLY' AND V_LAST_RUN + 30  <= SYSDATE THEN
        V_STATE_EXECUTE := 'Y';
    ELSE
        V_STATE_EXECUTE := 'N';
    END IF;  

    IF  V_STATE_EXECUTE ='Y' THEN
        IF  V_RUN_STAT='R' THEN
            o_status:=c_status_error;
            o_msg:='Load already running';

        ELSIF  V_RUN_STAT='Y' THEN
            UPDATE JOB_INFO SET JI_LAST_RUN_STATUS = 'R'
            WHERE JI_NAME = 'ARC_ON_QUOTE_MASTER_TABLE'
            AND JI_TYPE = 'ARCHIVAL';

            SELECT ao_object_name,ao_grace_period,ao_object_type,ao_skip_flag,
            replace(ao_condition,':AO_GRACE_PERIOD',ao_grace_period)
            BULK COLLECT INTO  objectname,graceperiod,objecttype,aoskipflag,conditions
            from ARCHIVAL_OBJECTS where ao_skip_flag='N' and ao_object_type='QUOTE_MT';

            if objectname.count>0 then
 
                FOR i IN objectname.FIRST .. objectname.LAST
                LOOP
                     BEGIN                    
                        if objectname(i)='ARCHIVE_QUOTE_MASTER_TABLES' then
                            Query_exec:='select sls_qtn_id , sls_qtn_vrsn_sqn_nr from  SLS_QTN_VRSN where ' || conditions(i);
                            EXECUTE IMMEDIATE Query_exec bulk collect into slsqtnid,slsqtnvrsnsqnnr;
                            
                            if slsqtnid.count>0 then
                                FOR j IN slsqtnid.FIRST .. slsqtnid.LAST
                                LOOP
                                    BEGIN
                                        DELETE FROM SLS_QTN_ITM_HRCHY WHERE SLS_QTN_ID=slsqtnid(j) AND SLS_QTN_VRSN_SQN_NR=slsqtnvrsnsqnnr(j);
                                        DELETE FROM QUOTE_ROLLOUT WHERE SLS_QTN_ID=slsqtnid(j) AND SLS_QTN_VRSN_SQN_NR=slsqtnvrsnsqnnr(j);
                                        DELETE FROM QUOTE_NCGROUPS WHERE SLS_QTN_ID=slsqtnid(j) AND SLS_QTN_VRSN_SQN_NR=slsqtnvrsnsqnnr(j);
                                        DELETE FROM QUOTE_ITEM_DISC_SCALE WHERE SLS_QTN_ID=slsqtnid(j) AND SLS_QTN_VRSN_SQN_NR=slsqtnvrsnsqnnr(j);
                                        DELETE FROM QUOTE_ITEM_COMMENT WHERE SLS_QTN_ID=slsqtnid(j) AND SLS_QTN_VRSN_SQN_NR=slsqtnvrsnsqnnr(j);
                                        DELETE FROM QUOTE_BUNDLE WHERE SLS_QTN_ID=slsqtnid(j) AND SLS_QTN_VRSN_SQN_NR=slsqtnvrsnsqnnr(j);
                                        DELETE FROM QUOTE_ITEM WHERE SLS_QTN_ID=slsqtnid(j) AND SLS_QTN_VRSN_SQN_NR=slsqtnvrsnsqnnr(j);
                                        DELETE FROM SLS_QTN_ITM_PRC_ADJ WHERE SLS_QTN_ID=slsqtnid(j) AND SLS_QTN_VRSN_SQN_NR=slsqtnvrsnsqnnr(j);
                                        DELETE FROM SLS_QTN_ITEM WHERE SLS_QTN_ID=slsqtnid(j) AND SLS_QTN_VRSN_SQN_NR=slsqtnvrsnsqnnr(j);
                                        DELETE FROM QUOTE_RESELLER WHERE SLS_QTN_ID=slsqtnid(j) AND SLS_QTN_VRSN_SQN_NR=slsqtnvrsnsqnnr(j);
                                        DELETE FROM QUOTE_ATTACHMENTS WHERE SLS_QTN_ID=slsqtnid(j) AND SLS_QTN_VRSN_SQN_NR=slsqtnvrsnsqnnr(j);                    
                                        DELETE FROM QUOTE_AFFILIATES WHERE SLS_QTN_ID=slsqtnid(j) AND SLS_QTN_VRSN_SQN_NR=slsqtnvrsnsqnnr(j);
                                        DELETE FROM QUOTE_MULTI_COUNTRY WHERE SLS_QTN_ID=slsqtnid(j) AND SLS_QTN_VRSN_SQN_NR=slsqtnvrsnsqnnr(j);
                                        DELETE FROM QUOTE_ORDER WHERE SLS_QTN_ID=slsqtnid(j) AND SLS_QTN_VRSN_SQN_NR=slsqtnvrsnsqnnr(j);
                                        DELETE FROM QUOTE_TO_QUOTE_LINK WHERE SLS_QTN_ID=slsqtnid(j) AND SLS_QTN_VRSN_SQN_NR=slsqtnvrsnsqnnr(j);
                                        DELETE FROM SLS_QTN_VRSN_CMT WHERE SLS_QTN_ID=slsqtnid(j) AND SLS_QTN_VRSN_SQN_NR=slsqtnvrsnsqnnr(j);
                                        DELETE FROM QUOTE_CUSTOMER_CONTACT WHERE SLS_QTN_ID=slsqtnid(j) AND SLS_QTN_VRSN_SQN_NR=slsqtnvrsnsqnnr(j);
                                        DELETE FROM QUOTE_CUSTOMER_ADDRESS WHERE SLS_QTN_ID=slsqtnid(j) AND SLS_QTN_VRSN_SQN_NR=slsqtnvrsnsqnnr(j);
                                        DELETE FROM QUOTE_CONTACT WHERE SLS_QTN_ID=slsqtnid(j) AND SLS_QTN_VRSN_SQN_NR=slsqtnvrsnsqnnr(j);
                                        DELETE FROM QUOTE_CUSTOMER WHERE SLS_QTN_ID=slsqtnid(j) AND SLS_QTN_VRSN_SQN_NR=slsqtnvrsnsqnnr(j);
                                        DELETE FROM QUOTE_VERSION WHERE SLS_QTN_ID=slsqtnid(j) AND SLS_QTN_VRSN_SQN_NR=slsqtnvrsnsqnnr(j);
                                        select count(1) into l_cnt  from quote_version where SLS_QTN_ID=slsqtnid(j);
                                        if l_cnt = 0 then
                                            DELETE FROM QUOTE WHERE SLS_QTN_ID=slsqtnid(j);
                                            l_cnt  := 0;
                                        end if ;
                                        
                                        DELETE FROM SLS_QTN_SECTION WHERE SLS_QTN_ID=slsqtnid(j) AND SLS_QTN_VRSN_SQN_NR=slsqtnvrsnsqnnr(j);
                                        DELETE FROM SLS_QTN_VRSN WHERE SLS_QTN_ID=slsqtnid(j) AND SLS_QTN_VRSN_SQN_NR=slsqtnvrsnsqnnr(j);  
                                        select count(1) into l_cnt  from SLS_QTN_VRSN  where SLS_QTN_ID=slsqtnid(j);
                                        if l_cnt = 0 then
                                            DELETE FROM SLS_QTN WHERE SLS_QTN_ID=slsqtnid(j);
                                            l_cnt  := 0;
                                        end if ;  
                                        
                                        DELETE FROM QUOTE_VERSION_CONFIG WHERE SLS_QTN_ID=slsqtnid(j) AND SLS_QTN_VRSN_SQN_NR=slsqtnvrsnsqnnr(j);
                                        DELETE FROM QUOTE_SECTION WHERE SLS_QTN_ID=slsqtnid(j) AND SLS_QTN_VRSN_SQN_NR=slsqtnvrsnsqnnr(j);                  
                                        COMMIT;
                                    END;
                                END LOOP;
                            end if;
                        end if;
                    END;
                END LOOP;
              
                UPDATE JOB_INFO
                SET JI_LAST_RUN_STATUS = 'Y', JI_LAST_RUN_DATE=trunc(sysdate)
                WHERE JI_NAME = 'ARC_ON_QUOTE_MASTER_TABLE' AND JI_TYPE = 'ARCHIVAL';
                COMMIT;

                o_status:= c_status_ok;
            end if;    
        end if;
    END IF;

EXCEPTION

    WHEN OTHERS 
    THEN
        ROLLBACK;
        UPDATE JOB_INFO SET JI_LAST_RUN_STATUS = 'N' WHERE JI_NAME = 'ARC_ON_QUOTE_MASTER_TABLE' AND JI_TYPE = 'ARCHIVAL';
        COMMIT;

        o_status:=c_status_error;
        o_msg:='Error while processing records' ||':'|| TO_CHAR(SQLCODE) || ':' || SQLERRM;
        raise_application_error ( -20001 ,'Archival on quote master tables Failed'); 

END ARC_ON_QUOTE_MASTER_TABLES;


PROCEDURE PURGE_ON_QUOTE_DC_TABLES(o_status OUT VARCHAR2, o_msg OUT VARCHAR2)
AS

    Query_exec VARCHAR2(4000);

    TYPE objectnameTab IS TABLE OF ARCHIVAL_OBJECTS.AO_OBJECT_NAME%TYPE;
    objectname objectnameTab := objectnameTab();

    TYPE  graceperiodTab IS TABLE OF ARCHIVAL_OBJECTS.AO_GRACE_PERIOD%TYPE;
    graceperiod graceperiodTab := graceperiodTab();

    TYPE conditionTab IS TABLE OF ARCHIVAL_OBJECTS.AO_CONDITION%TYPE;
    conditions conditionTab := conditionTab();

    TYPE objecttypeTab IS TABLE OF ARCHIVAL_OBJECTS.AO_OBJECT_TYPE%TYPE;
    objecttype objecttypeTab := objecttypeTab();

    TYPE aoskipflagTab IS TABLE OF ARCHIVAL_OBJECTS.AO_SKIP_FLAG%TYPE;
    aoskipflag aoskipflagTab := aoskipflagTab();

    TYPE reeventidTab IS TABLE OF REPLICATION_EVENT.RE_EVENT_ID%TYPE;
    reeventid reeventidTab := reeventidTab();

    TYPE slsqtnidTab IS TABLE OF SLS_QTN_VRSN.SLS_QTN_ID%TYPE;
    slsqtnid slsqtnidTab := slsqtnidTab();

    TYPE slsqtnsqnnridTab IS TABLE OF SLS_QTN_VRSN.SLS_QTN_VRSN_SQN_NR%TYPE;
    slsqtnvrsnsqnnr slsqtnsqnnridTab := slsqtnsqnnridTab();
    
    TYPE evfloadidTab IS TABLE OF EVF_DATA_LOAD_EVENT.EVF_LOAD_ID%TYPE;
    evfloadid evfloadidTab := evfloadidTab();
    
    TYPE evfsourceTab IS TABLE OF EVF_DATA_LOAD_EVENT.EVF_SOURCE%TYPE;
    evfsource evfsourceTab := evfsourceTab();

    V_LAST_RUN DATE;

    V_RUN_STAT VARCHAR2(1);

    V_ARCHIVE_FREQUENCY VARCHAR2(50);
  
BEGIN

    SELECT  JI_LAST_RUN_DATE , JI_LAST_RUN_STATUS, JI_ARCHIVE_FREQUENCY INTO V_LAST_RUN, V_RUN_STAT, V_ARCHIVE_FREQUENCY FROM JOB_INFO
              WHERE JI_NAME = 'PURGE_ON_QUOTE_DC_TABLE' AND JI_TYPE = 'ARCHIVAL';

    IF V_ARCHIVE_FREQUENCY = 'DAILY' AND V_LAST_RUN + 1  <= SYSDATE THEN
        IF  V_RUN_STAT='R' THEN
            o_status:=c_status_error;
            o_msg:='Load already running';

        ELSIF  V_RUN_STAT='Y' THEN

            UPDATE JOB_INFO SET JI_LAST_RUN_STATUS = 'R'
            WHERE JI_NAME = 'PURGE_ON_QUOTE_DC_TABLE'
            AND JI_TYPE = 'ARCHIVAL';


            SELECT ao_object_name,ao_grace_period,ao_object_type,ao_skip_flag,
            replace(ao_condition,':AO_GRACE_PERIOD',ao_grace_period)
            BULK COLLECT INTO  objectname,graceperiod,objecttype,aoskipflag,conditions
            from ARCHIVAL_OBJECTS
            where ao_skip_flag='N' and ao_object_type='QUOTE_DC';

            if objectname.count>0 then

                FOR i IN objectname.FIRST .. objectname.LAST
                LOOP
                    BEGIN
                        if objectname(i)='PURGE_REPLICATION_EVENT' then
                            Query_exec:= 'select RE_EVENT_ID from  REPLICATION_EVENT where '  || conditions(i);
                            EXECUTE IMMEDIATE Query_exec bulk collect into reeventid;
                        
                            if reeventid.count>0 then
                                FOR j IN reeventid.FIRST .. reeventid.LAST
                                    LOOP
                                        BEGIN
                                            DELETE FROM REPLICATION_EVENT WHERE RE_EVENT_ID=reeventid(j);
                                            COMMIT;
                                        END;
                                    END LOOP;
                            end if;
                        elsif objectname(i)='PURGE_QUOTE_DC_TABLES' then
                            Query_exec:= 'select evf_load_id, evf_source from  EVF_DATA_LOAD_EVENT where ' || conditions(i);
                            EXECUTE IMMEDIATE Query_exec bulk collect into evfloadid,evfsource;                            

                            if evfloadid.count>0 then
                                FOR k IN evfloadid.FIRST .. evfloadid.LAST
                                LOOP
                                    BEGIN
                                        if evfsource(k)='Watson' then                                    
                                            DELETE FROM WATSON_QUOTE_CUSTOMER_TYPE_DC WHERE trn_id=evfloadid(k);
                                            DELETE FROM WATSON_CUSTOMER_ADDRESS_DC WHERE trn_id=evfloadid(k);
                                            DELETE FROM WATSON_CUSTOMER_CONTACT_DC WHERE trn_id=evfloadid(k);
                                            DELETE FROM WATSON_QUOTE_ITEM_DC WHERE trn_id=evfloadid(k);
                                            DELETE FROM WATSON_ORDER_REF_DC WHERE trn_id=evfloadid(k);
                                            DELETE FROM WATSON_QUOTE_VERSION_DC WHERE trn_id=evfloadid(k);
                                        elsif evfsource(k)='Eclipse' then
                                            DELETE FROM ECLIPSE_AFFILIATES_DC WHERE trn_id=evfloadid(k);
                                            DELETE FROM ECLIPSE_ATTACHMENTS_DC WHERE trn_id=evfloadid(k);
                                            DELETE FROM ECLIPSE_BUNDLE_DC WHERE trn_id=evfloadid(k);
                                            DELETE FROM ECLIPSE_DEAL_COMMENTS_DC WHERE trn_id=evfloadid(k);                                    
                                            DELETE FROM ECLIPSE_LINE_COMMENTS_DC WHERE trn_id=evfloadid(k);
                                            DELETE FROM ECLIPSE_LINE_DISC_SCALE_DC WHERE trn_id=evfloadid(k);
                                            DELETE FROM ECLIPSE_LINE_ITEM_DC WHERE trn_id=evfloadid(k);
                                            DELETE FROM ECLIPSE_MULTI_COUNTRY_DC WHERE trn_id=evfloadid(k);
                                            DELETE FROM ECLIPSE_NCGROUPS_DC WHERE trn_id=evfloadid(k);
                                            DELETE FROM ECLIPSE_RESELLER_DC WHERE trn_id=evfloadid(k);
                                            DELETE FROM ECLIPSE_ROLLOUT_DC WHERE trn_id=evfloadid(k);
                                            DELETE FROM ECLIPSE_DEAL_STATUS_DC WHERE trn_id=evfloadid(k);
                                            DELETE FROM ECLIPSE_DEAL_HEADER_DC WHERE trn_id=evfloadid(k);
                                        end if; 
                                
                                        DELETE FROM EVF_BATCH_QUEUE where evfbq_load_id=evfloadid(k);

                                        DELETE FROM EVF_DATA_LOAD_EVENT where evf_load_id=evfloadid(k);
                                        COMMIT;                                                        
                                    END;
                                END LOOP;
                            end if;
                        end if;
                    END;
                END LOOP;
                
                UPDATE JOB_INFO
                SET JI_LAST_RUN_STATUS = 'Y', JI_LAST_RUN_DATE=trunc(sysdate)
                WHERE JI_NAME = 'PURGE_ON_QUOTE_DC_TABLE' AND JI_TYPE = 'ARCHIVAL';

                COMMIT;

                o_status:= c_status_ok;
            end if;
        END IF;
    END IF;

EXCEPTION

    WHEN OTHERS THEN
        ROLLBACK;

        UPDATE JOB_INFO
            SET JI_LAST_RUN_STATUS = 'N'
            WHERE JI_NAME = 'PURGE_ON_QUOTE_DC_TABLE' AND JI_TYPE = 'ARCHIVAL';
        COMMIT;

        o_status:=c_status_error;
        o_msg:='Error while processing records' ||':'|| TO_CHAR(SQLCODE) || ':' || SQLERRM;
        raise_application_error ( -20001 ,'Purge on Quote DC tables Failed');

END PURGE_ON_QUOTE_DC_TABLES;



PROCEDURE archive_quoteIds_pdf_files (
   quoteId                     IN     quote_version.sls_qtn_id%TYPE,
   inputCondition         IN     VARCHAR2,
   status                         OUT VARCHAR2,
   msg                            OUT VARCHAR2,
   x out   pdf_renamed_ref_id_list,
   y out countrycode_list
   )
AS
   Query_exec            VARCHAR2 (4000);
   loopInput            VARCHAR2 (4000);
   condition             VARCHAR2 (4000);

   TYPE pdf_list IS TABLE OF quote_version.pdf_ref_id%TYPE;
   pdf_name              pdf_list := pdf_list ();

   TYPE customer_request_ts_list IS TABLE OF watson_quote_Version_dc.customer_request_ts%TYPE;
   customer_request_ts   customer_request_ts_list
                            := customer_request_ts_list ();

BEGIN
   status := 'Success';
 

  x  :=  pdf_renamed_ref_id_list(); -- :=pdf_renamed_ref_id_list ();
  y  := countrycode_list() ; -- := countrycode_list ();
  
  

   IF inputCondition in ('noOfDaysAfterExpiry')
   THEN
      BEGIN
         SELECT REPLACE (
                   (SELECT ao_condition
                      FROM archival_objects
                     WHERE ao_object_name = 'PDF_ARCHIVE_AFTER_EXPIRY_DATE'),
                   ':AO_GRACE_PERIOD',
                   (SELECT ao_grace_period
                      FROM archival_objects
                     WHERE ao_object_name = 'PDF_ARCHIVE_AFTER_EXPIRY_DATE'))
           INTO condition
           FROM DUAL;


         Query_exec :=
            'SELECT TY_pdf_renamed_ref_id(pdf_renamed_ref_id),
                  pdf_ref_id,
                  TY_COUNTRYCODE(countrycode),
                  MAX (customer_request_ts)
                     FROM watson_quote_Version_dc
                    WHERE pdf_ref_id IN
                             (SELECT pdf_ref_id
                                FROM QUOTE_IDS.quote_version
                               WHERE sls_qtn_id IN
                                        (SELECT sls_qtn_id
                                           FROM sls_qtn_vrsn
                                          WHERE '
            || condition
            || '))  AND pdf_ref_id IS NOT NULL  GROUP BY pdf_renamed_ref_id, pdf_ref_id, countrycode';
            
            dbms_output.put_line(Query_exec);

         EXECUTE IMMEDIATE Query_exec BULK COLLECT INTO x, pdf_name,  y,  customer_request_ts;
         
         dbms_output.put_line(x.count);
         
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            status := 'Fail';
            msg := SUBSTR (SQLERRM, 1, 100);
      END;
   END IF;


   IF inputCondition in ('noOfDaysAfterLastModified')
   THEN
      BEGIN
         SELECT REPLACE (
                   (SELECT ao_condition
                      FROM archival_objects
                     WHERE ao_object_name = 'PDF_ARCHIVE_AFTER_LAST_MODIFY'),
                   ':AO_GRACE_PERIOD',
                   (SELECT ao_grace_period
                      FROM archival_objects
                     WHERE ao_object_name = 'PDF_ARCHIVE_AFTER_LAST_MODIFY'))
           INTO condition
           FROM DUAL;


         Query_exec :=
            'SELECT TY_pdf_renamed_ref_id(pdf_renamed_ref_id),
                  pdf_ref_id,
                  TY_COUNTRYCODE(countrycode),
                  MAX (customer_request_ts)
                          FROM (SELECT pdf_renamed_ref_id,
                          pdf_ref_id,
                          countrycode,
                          customer_request_ts
                     FROM watson_quote_Version_dc
                    WHERE pdf_ref_id IN
                             (SELECT pdf_ref_id
                                FROM QUOTE_IDS.quote_version
                               WHERE '
            || condition
            || ')  AND pdf_ref_id IS NOT NULL)  GROUP BY pdf_renamed_ref_id, pdf_ref_id, countrycode';
            
        dbms_output.put_line(Query_exec);            

         EXECUTE IMMEDIATE Query_exec BULK COLLECT INTO x,pdf_name, y,  customer_request_ts;
         
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            status := 'Fail';
            msg := SUBSTR (SQLERRM, 1, 100);
      END;
   END IF;

   IF inputCondition in ('noOfLastVersion')  
   THEN
   BEGIN
      SELECT REPLACE ( (SELECT ao_condition
                          FROM archival_objects
                         WHERE ao_object_name = 'PDF_ARCHIVE_LAST_VERSION'),
                      ':AO_GRACE_PERIOD',
                      (SELECT ao_grace_period
                         FROM archival_objects
                        WHERE ao_object_name = 'PDF_ARCHIVE_LAST_VERSION'))
        INTO condition
        FROM DUAL;

      FOR i IN REVERSE 1 .. condition
      LOOP
         DBMS_OUTPUT.PUT_LINE ('Loop counter is ' || i);

         BEGIN
            loopInput :=  loopInput || ','||   i  ;
         END;
      END LOOP;
      
       Query_exec :=
            'SELECT TY_pdf_renamed_ref_id(pdf_renamed_ref_id),
                  pdf_ref_id,
                  TY_COUNTRYCODE(countrycode),
                  MAX (customer_request_ts)
              FROM (SELECT pdf_renamed_ref_id,
                          pdf_ref_id,
                          countrycode,
                          customer_request_ts
                     FROM watson_quote_Version_dc
                    WHERE pdf_ref_id IN
                             (SELECT pdf_ref_id
                                FROM QUOTE_IDS.quote_version
                               WHERE  sls_qtn_vrsn_sqn_nr  IN (' 
            || loopInput || ' )   sls_qtn_id = ' ||  quoteId 
            || ')  AND pdf_ref_id IS NOT NULL)  GROUP BY pdf_renamed_ref_id, pdf_ref_id, countrycode';

         EXECUTE IMMEDIATE Query_exec BULK COLLECT INTO x,pdf_name, y,customer_request_ts;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               status := 'Fail';
               msg := SUBSTR (SQLERRM, 1, 100);
          END;  
      END IF;

   DBMS_OUTPUT.put_line ('status is ' || status);

END archive_quoteIds_pdf_files;



END ARCH_QUOTE_IDS;
/