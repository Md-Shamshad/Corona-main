declare
o_status varchar2(1000);
o_msg varchar2(1000);
begin
ARCH_QUOTE_IDS.PURGE_ON_QUOTE_DC_TABLES (o_status,o_msg);
end;
/