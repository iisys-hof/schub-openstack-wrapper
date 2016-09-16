package de.hofuniversity.iisys.schub.openstack.util;

public class ServiceConstants
{
    // Weave service
    public static final String WEAVE_SERVICE = "weave";
    public static final String WEAVE_PEERS = "weave_peers";
    public static final String WEAVE_PASSWORD = "weave_password";
    public static final String WEAVE_NETWORK = "weave_network";
    public static final String WEAVE_HOST_IP = "weave_host_ip";
    public static final String WEAVE_HOST_NETWORK = "weave_host_net";
    
    // Camunda
    public static final String CAMUNDA_SERVICE = "camunda";
    
    public static final String CAMUNDA_DB_USER = "INSERT_DB_USER_HERE";
    public static final String CAMUNDA_DB_PASSWORD = "INSERT_DB_PASSWORD_HERE";
    public static final String CAMUNDA_DB_NAME = "INSERT_DB_NAME_HERE";
    public static final String CAMUNDA_DB_HOST = "INSERT_DB_HOST_HERE";
    public static final String CAMUNDA_DB_PORT = "INSERT_DB_PORT_HERE";
    
    public static final String CAMUNDA_LDAP_SERVER = "INSERT_LDAP_SERVER_HERE";
    public static final String CAMUNDA_LDAP_BASE_DN = "INSERT_LDAP_BASE_DN_HERE";
    public static final String CAMUNDA_LDAP_USER = "INSERT_LDAP_USER_HERE";
    public static final String CAMUNDA_LDAP_PASSWORD = "INSERT_LDAP_PASSWORD_HERE";
    public static final String CAMUNDA_LDAP_USER_DN = "INSERT_LDAP_USER_DN_HERE";

    public static final String CAMUNDA_DEFAULT_ADMIN = "INSERT_DEFAULT_ADMIN_HERE";
    public static final String CAMUNDA_CAS_LOGIN_URL = "INSERT_CAS_LOGIN_URL_HERE";
    public static final String CAMUNDA_CAS_SERVER_URL = "INSERT_CAS_SERVER_URL_HERE";
    public static final String CAMUNDA_SERVER_NAME = "INSERT_SERVER_NAME_HERE";
    public static final String CAMUNDA_URL = "INSERT_CAMUNDA_URL_HERE";
    public static final String CAMUNDA_SHINDIG_URL = "INSERT_SHINDIG_URL_HERE";
    
    // CAS
    public static final String CAS_SERVICE = "cas";
    
    public static final String CAS_LDAP_SERVER = "INSERT_LDAP_SERVER_HERE";
    public static final String CAS_LDAP_USER = "INSERT_LDAP_USER_HERE";
    public static final String CAS_LDAP_PASSWORD = "INSERT_LDAP_PASSWORD_HERE";
    public static final String CAS_LDAP_USER_DN = "INSERT_USER_BASE_DN_HERE";
    
    public static final String CAS_SERVER_NAME = "INSERT_SERVER_NAME_HERE";
    public static final String CAS_NODE_NAME = "INSERT_NODE_NAME_HERE";
    public static final String CAS_ALLOWED_SERVICES = "INSERT_ALLOWED_SERVICE_IDS_HERE";
    public static final String CAS_LIFERAY_CALLBACK = "INSERT_LIFERAY_PGT_CALLBACK_HERE";
    public static final String CAS_OX_CALLBACK = "INSERT_OX_PGT_CALLBACK_HERE";
    
    // Elasticsearch 1.x
    public static final String ELASTICSEARCH_SERVICE = "elasticsearch";

    // Elasticsearch 2.x
    public static final String ELASTICSEARCH2_SERVICE = "elasticsearch2";
    
    // Elasticsearch shared
    public static final String ELASTICSEARCH_CL_NAME = "INSERT_CLUSTER_NAME_HERE";
    public static final String ELASTICSEARCH_NODE_NAME = "INSERT_NODE_NAME_HERE";
    public static final String ELASTICSEARCH_HEAP_SIZE = "INSERT_HEAP_SIZE_HERE";
    
    // Liferay
    public static final String LIFERAY_SERVICE = "liferay";
    
    public static final String LIFERAY_DB_USER = "INSERT_DB_USER_HERE";
    public static final String LIFERAY_DB_PASSWORD = "INSERT_DB_PASSWORD_HERE";
    public static final String LIFERAY_DB_NAME = "INSERT_DB_NAME_HERE";
    public static final String LIFERAY_DB_HOST = "INSERT_DB_SERVER_HERE";
    public static final String LIFERAY_DB_PORT = "INSERT_DB_PORT_HERE";
    
    public static final String LIFERAY_LDAP_SERVER="INSERT_LDAP_SERVER_HERE";
    public static final String LIFERAY_LDAP_BASE_DN="INSERT_LDAP_BASE_DN_HERE";
    public static final String LIFERAY_LDAP_USER="INSERT_LDAP_USER_HERE";
    public static final String LIFERAY_LDAP_PASSWORD="INSERT_LDAP_PASSWORD_HERE";
    public static final String LIFERAY_LDAP_USER_DN="INSERT_LDAP_USER_DN_HERE";
    
    public static final String LIFERAY_ADMIN_MAIL="INSERT_ADMIN_MAIL_HERE";
    public static final String LIFERAY_ADMIN_FULL_NAME="INSERT_ADMIN_FULL_NAME_HERE";
    public static final String LIFERAY_ADMIN_FIRST_NAME="INSERT_ADMIN_FIRST_NAME_HERE";
    public static final String LIFERAY_ADMIN_LAST_NAME="INSERT_ADMIN_LAST_NAME_HERE";
    public static final String LIFERAY_ADMIN_PASSWORD="INSERT_ADMIN_PASSWORD_HERE";
    public static final String LIFERAY_ADMIN_ID="INSERT_ADMIN_ID_HERE";
    
    public static final String LIFERAY_COMPANY_NAME = "INSERT_COMPANY_NAME_HERE";
    public static final String LIFERAY_COMPANY_WEBID = "INSERT_COMPANY_WEBID_HERE";
    
    public static final String LIFERAY_CAS_LOGIN_URL = "INSERT_CAS_LOGIN_URL_HERE";
    public static final String LIFERAY_CAS_LOGOUT_URL = "INSERT_CAS_LOGOUT_URL_HERE";
    public static final String LIFERAY_CAS_SERVER_URL = "INSERT_CAS_SERVER_URL_HERE";
    public static final String LIFERAY_CAS_SERVER_NAME = "INSERT_CAS_SERVER_NAME_HERE";
    public static final String LIFERAY_CAS_CLEARPASS_URL = "INSERT_CAS_CLEARPASS_URL_HERE";
    public static final String LIFERAY_LOGIN_URL = "INSERT_LIFERAY_LOGIN_URL_HERE";
    public static final String LIFERAY_PGT_CALLBACK_URL = "INSERT_PGT_CALLBACK_URL_HERE";
    public static final String LIFERAY_URL = "INSERT_LIFERAY_URL_HERE";
    
    public static final String LIFERAY_CMIS_USER = "INSERT_NUXEO_CMIS_USER_HERE";
    public static final String LIFERAY_CMIS_PASSWORD = "INSERT_NUXEO_CMIS_PASSWORD_HERE";
    public static final String LIFERAY_NUXEO_URL = "INSERT_NUXEO_URL_HERE";
    
    public static final String LIFERAY_ES_SERVER = "INSERT_ES2_SERVER_HERE";
    public static final String LIFERAY_ES_CLUSTER_NAME = "INSERT_ES2_CLUSTER_NAME_HERE";
    
    public static final String LIFERAY_SHINDIG_URL = "INSERT_SHINDIG_URL_HERE";
    public static final String LIFERAY_SHINDIG_TOKEN = "INSERT_SHINDIG_SEC_TOKEN_HERE";
    public static final String LIFERAY_SKILL_WIKI_URL = "INSERT_SKILL_WIKI_URL_HERE";
    public static final String LIFERAY_LINK_URL = "INSERT_LIFERAY_LINK_URL_HERE";

    // Nuxeo
    public static final String NUXEO_SERVICE = "nuxeo";
    
    public static final String NUXEO_DB_USER = "INSERT_DB_USER_HERE";
    public static final String NUXEO_DB_PASSWORD = "INSERT_DB_PASSWORD_HERE";
    public static final String NUXEO_DB_NAME = "INSERT_DB_NAME_HERE";
    public static final String NUXEO_DB_HOST = "INSERT_DB_HOST_HERE";
    public static final String NUXEO_DB_PORT = "INSERT_DB_PORT_HERE";

    public static final String NUXEO_LDAP_USER="INSERT_LDAP_USER_HERE";
    public static final String NUXEO_LDAP_PASSWORD="INSERT_LDAP_PASSWORD_HERE";
    public static final String NUXEO_LDAP_SERVER="INSERT_LDAP_SERVER_HERE";
    public static final String NUXEO_LDAP_USER_BASE="INSERT_LDAP_USER_BASE_HERE";

    public static final String NUXEO_ES_SERVER = "INSERT_ES_SERVER_HERE";
    public static final String NUXEO_ES_CLUSTER = "INSERT_ES_CLUSTER_NAME_HERE";
    
    public static final String NUXEO_CAS_APP_URL = "INSERT_CAS_APP_URL_HERE";
    public static final String NUXEO_CAS_LOGIN_URL = "INSERT_CAS_SVC_LOGIN_URL_HERE";
    public static final String NUXEO_CAS_VAL_URL = "INSERT_CAS_SVC_VALIDATE_URL_HERE";
    public static final String NUXEO_CAS_LOGOUT_URL = "INSERT_CAS_LOGOUT_URL_HERE";

    public static final String NUXEO_URL = "INSERT_NUXEO_URL_HERE";
    public static final String NUXEO_SHINDIG_URL = "INSERT_SHINDIG_URL_HERE";
    public static final String NUXEO_ACTIVITY_LANG = "INSERT_ACTIVITY_LANG_HERE";
    public static final String NUXEO_PROFILE_URL = "INSERT_PROFILE_URL_HERE";
    
    public static final String NUXEO_CAMUNDA_URL = "INSERT_CAMUNDA_URL_HERE";
    public static final String NUXEO_CAMUNDA_REST_URL = "INSERT_CAMUNDA_REST_URL_HERE";
    public static final String NUXEO_BPMN_JS_UTILS_URL = "INSERT_BPMN_JS_UTILS_URL_HERE";
    public static final String NUXEO_PROCESS_BASE_URL = "INSERT_PROCESS_BASE_URL_HERE";
    
    
    // Open-Xchange
    public static final String OPEN_XCHANGE_SERVICE = "open-xchange";
    
    public static final String OX_DB_USER = "INSERT_DB_USER_HERE";
    public static final String OX_DB_PASSWORD = "INSERT_DB_PASSWORD_HERE";
    public static final String OX_CONF_DB_NAME = "INSERT_DB_NAME_HERE";
    public static final String OX_DATA_DB_NAME = "INSERT_DATA_DB_NAME_HERE";
    public static final String OX_DB_HOST = "INSERT_DB_HOST_HERE";
    public static final String OX_DB_PORT = "INSERT_DB_PORT_HERE";
    
    public static final String OX_LDAP_SERVER = "INSERT_LDAP_SERVER_HERE";
    public static final String OX_LDAP_USER = "INSERT_LDAP_USER_HERE";
    public static final String OX_LDAP_PASSWORD = "INSERT_LDAP_PASSWORD_HERE";
    public static final String OX_LDAP_USER_BASE = "INSERT_LDAP_USER_BASE_HERE";
    
    public static final String OX_SERVER_NAME = "INSERT_SERVER_NAME_HERE";
    public static final String OX_MASTER_PASS = "INSERT_MASTER_PASSWORD_HERE";
    public static final String OX_APPSUITE_URL = "INSERT_APPSUITE_URL_HERE";
    public static final String OX_SHINDIG_URL = "INSERT_SHINDIG_URL_HERE";
    
    public static final String OX_CAS_URL = "INSERT_CAS_URL_HERE";
    public static final String OX_CAS_CLEARPASS_URL = "INSERT_CAS_CLEARPASS_URL_HERE";
    public static final String OX_CAS_AUTH_URL = "INSERT_OX_CAS_AUTH_URL_HERE";
    public static final String OX_CLEARPASS_CALLBACK = "INSERT_CLEARPASS_CALLBACK_HERE";
    
    public static final String OX_ADMIN_MAIL = "INSERT_ADMIN_MAIL_HERE";
    public static final String OX_ADMIN_PASSWORD = "INSERT_ADMIN_PASSWORD_HERE";
    
    public static final String OX_IMAP_SERVER = "INSERT_IMAP_SERVER_HERE";
    public static final String OX_SMTP_SERVER = "INSERT_SMTP_SERVER_HERE";
    public static final String OX_IMAP_MASTER_PW = "INSERT_MAIL_MASTER_PASSWORD_HERE";
    
    // Shindig
    public static final String SHINDIG_SERVICE = "shindig";
    
    public static final String SHINDIG_SEC_TOKEN = "INSERT_SEC_TOKEN_HERE";
    
    public static final String SHINDIG_ES_HOST = "INSERT_ES_HOST_HERE";
    public static final String SHINDIG_ES_PORT = "INSERT_ES_PORT_HERE";
    public static final String SHINDIG_ES_CL_NAME = "INSERT_ES_CLUSTER_NAME_HERE";
    
    public static final String SHINDIG_WS_SERVER = "INSERT_WS_SERVER_HERE";
    public static final String SHINDIG_WS_CONNS = "INSERT_WS_CONNECTIONS_HERE";
    
    // Websocket Server
    public static final String WEBSOCKET_SERVER_SERVICE = "websocket-server";

    public static final String WEBSOCKET_THREADS = "INSERT_THREADS_HERE";
    
    public static final String WEBSOCKET_ORG_NAME = "INSERT_ORG_NAME_HERE";
    public static final String WEBSOCKET_ORG_FIELD = "INSERT_ORG_FIELD_HERE";
    public static final String WEBSOCKET_ORG_SUBFIELD = "INSERT_ORG_SUBFIELD_HERE";
    public static final String WEBSOCKET_ORG_WEBPAGE = "INSERT_ORG_WEBPAGE_HERE";
    
    // Nginx Server
    public static final String NGINX_SERVICE = "nginx";
    
    public static final String NGINX_TENANT_NAME = "INSERT_TENANT_NAME_HERE";
    public static final String NGINX_LOCAL_DOMAIN = "INSERT_LOCAL_DOMAIN_HERE";
    public static final String NGINX_WEAVE_DNS = "INSERT_WEAVE_DNS_HERE";
    public static final String NGINX_MAX_REQUEST_SIZE = "INSERT_MAX_REQUEST_SIZE_HERE";
    
    // Elasticsearch Relay
    public static final String ES_RELAY_SERVICE = "es-relay";
    
    public static final String ES_RELAY_ES_URL = "INSERT_ES_URL_HERE";
    public static final String ES_RELAY_ES_HOST = "INSERT_ES_HOST_HERE";
    public static final String ES_RELAY_ES_PORT = "INSERT_ES_PORT_HERE";
    public static final String ES_RELAY_ES_CLUSTER_NAME = "INSERT_ES_CLUSTER_NAME_HERE";
    
    public static final String ES_RELAY_ES2_URL = "INSERT_ES2_URL_HERE";
    public static final String ES_RELAY_ES2_HOST = "INSERT_ES2_HOST_HERE";
    public static final String ES_RELAY_ES2_PORT = "INSERT_ES2_PORT_HERE";
    public static final String ES_RELAY_ES2_CLUSTER_NAME = "INSERT_ES2_CLUSTER_NAME_HERE";
    
    public static final String ES_RELAY_LR_INDEX = "INSERT_LIFERAY_INDEX_HERE";
    public static final String ES_RELAY_LR_URL = "INSERT_LIFERAY_URL_HERE";
    public static final String ES_RELAY_LR_COMP_ID = "INSERT_LIFERAY_COMPANY_ID_HERE";
    public static final String ES_RELAY_LR_USER = "INSERT_LIFERAY_USER_HERE";
    public static final String ES_RELAY_LR_PASS = "INSERT_LIFERAY_PASSWORD_HERE";
    public static final String ES_RELAY_LR_PASS_ROLES = "INSERT_LIFERAY_PASS_ROLES_HERE";
    
    public static final String ES_RELAY_NX_URL = "INSERT_NUXEO_URL_HERE";
    public static final String ES_RELAY_NX_USER = "INSERT_NUXEO_USER_HERE";
    public static final String ES_RELAY_NX_PASS = "INSERT_NUXEO_PASSWORD_HERE";
    
    public static final String ES_RELAY_SHIN_URL = "INSERT_SHINDIG_URL_HERE";
    
    public static final String ES_RELAY_CAS_LOGIN_URL = "INSERT_CAS_LOGIN_URL_HERE";
    public static final String ES_RELAY_CAS_URL = "INSERT_CAS_SERVER_URL_HERE";
    public static final String ES_RELAY_CAS_SRV_NAME = "INSERT_SERVER_NAME_HERE";
    
    // general
    public static final String SERVICE_ID_PROP = "INSERT_SERVICE_ID_HERE";
    
    public static final String SERVICE_TENANT_PROP = "INSERT_TENANT_HERE";
    public static final String SERVICE_TENANT_VM_PROP = "INSERT_TENANT_VM_HERE";
    
    public static final String HOSTNAME_PROP = "INSERT_SERVER_HOSTNAME_HERE";
    
    public static final String MEM_PROP = "INSERT_MEMORY_HERE";
    public static final String CPUS_PROP = "INSERT_CPUS_HERE";
    
    public static final String JAVA_MEM_MIN_PROP = "INSERT_JAVA_MEM_MIN_HERE";
    public static final String JAVA_MEM_MAX_PROP = "INSERT_JAVA_MEM_MAX_HERE";
    
    public static final String CERTS_PATH_PROP = "INSERT_CERTIFICATES_PATH_HERE";
    
    
    public static final String TOMCAT_HTTPS_PORT = "8443";
    
    
    public static final String STATUS_STOPPED = "stopped";
    public static final String STATUS_RUNNING = "running";
    public static final String STATUS_STAGED = "staged";
    public static final String STATUS_WAITING = "waiting";
    public static final String STATUS_ERROR = "error";
    
    
    public static final String[] SERVICES = {CAMUNDA_SERVICE, CAS_SERVICE,
        ELASTICSEARCH_SERVICE, ELASTICSEARCH2_SERVICE, LIFERAY_SERVICE,
        NUXEO_SERVICE, OPEN_XCHANGE_SERVICE, SHINDIG_SERVICE,
        WEBSOCKET_SERVER_SERVICE, NGINX_SERVICE, ES_RELAY_SERVICE};
}
