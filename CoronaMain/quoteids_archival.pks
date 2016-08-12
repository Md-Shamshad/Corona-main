CREATE OR REPLACE PACKAGE QUOTE_IDS.ARCH_QUOTE_IDS
IS

c_status_error VARCHAR2(1000) := 'err';
c_status_ok VARCHAR2(1000) := 'success';

PROCEDURE ARC_ON_QUOTE_MASTER_TABLES(o_status OUT VARCHAR2, o_msg OUT VARCHAR2);

PROCEDURE PURGE_ON_QUOTE_DC_TABLES(o_status OUT VARCHAR2, o_msg OUT VARCHAR2);

PROCEDURE archive_quoteIds_pdf_files (
   quoteId                     IN     quote_version.sls_qtn_id%TYPE,
   inputCondition         IN     VARCHAR2,
   status                         OUT VARCHAR2,
   msg                            OUT VARCHAR2,
   x out   pdf_renamed_ref_id_list,
   y out countrycode_list
   );


END ARCH_QUOTE_IDS;
/