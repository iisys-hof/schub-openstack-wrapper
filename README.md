# schub-openstack-wrapper
Wrapper around OpenStack and the SCHub cluster base offering a RESTful API to create, update and delete tenants

### Installation from Source

Adapt the configuration to your environment: /src/main/resources/openstack-wrapper.properties

Target Host Requirements:
* Tomcat 7 or 8
  * Default path: /home/ubuntu/schub-openstack-wrapper/tomcat/
* SCHub Image Generation and Utility Scripts
  * see https://github.com/iisys-hof/schub-automation-resources#docker-image-generation
  * Default path: /home/ubuntu/image-gen/
  * Still requires configuration and an automated certificate authority (see https://github.com/iisys-hof/schub-automation-resources#docker-image-generation)
* SCHub VM and service templates
  * based on https://github.com/iisys-hof/schub-automation-resources/tree/master/templates/marathon and https://github.com/iisys-hof/schub-automation-resources/tree/master/templates/heat
  * Default path: /home/ubuntu/schub-openstack-wrapper/
  * May also still need to be adapted to the environment

Installation: 
* Build using maven with package goal
* Rename war file in target folder to schub.war
* Deploy in Tomcat

The interface will then be available directly under http(s)://$HOST:$PORT/$CONTEXT/

### API

The interface is available under http(s)://$HOST:$PORT/$CONTEXT/api/

Tenants:
* GET /api/tenants/
  * get all tenants (ID, name, console name)
* GET /api/tenants/$TENANT_ID/
  * get the details (including VM IDs) for the tenant with the given ID
* POST /api/tenants/
  * creates a tenant
  * parameters (JSON object body):
      * name
      * consoleName (a simple low level console-compatible name)
      * description
      * availabilityZone
* DELETE /api/tenants/$TENANT_ID
  * deletes a tenant

Virtual machines:
* GET /api/machines/
  * get all virtual machines
* GET /api/machines/$VM_ID/
  * get details for the VM with the given ID
* GET /api/machines/$VM1_ID,$VM2_ID,$VM3_ID/
  * get details for the VMs with the given IDs
* POST /api/machines/
  * creates a virtual machine for a tenant
  * parameters (JSON object body):
      * name (a simple low level console-compatible name)
      * tenantId
      * flavorName (OpenStack flavor / VM size)
      * services (JSON array of services to start using their current/default configuration)
      * virt_cpus (number of CPU cores to offer mesos, for overprovisioning)
      * virt_ram (amount of RAM in megabytes to offer mesos, for overprovisioning)
      * virt_disk (amount of disk space in megabytes to offer mesos, for overprovisioning)
* DELETE /api/machines/$VM_ID
  * delete a virtual machine

Services:
* GET /api/services/
  * get a list of all known services
* GET /api/services/$TENANT_ID/
  * get a list of all services for a tenant, including their current status
* GET /api/services/$TENANT_ID/$SERVICE_NAME/
  * get the full current / default configuration for a service
* POST /api/services/$TENANT_ID/$SERVICE_NAME/
  * creates and starts a service for a tenant
  * parameters (JSON array with parameters as "name" and "value" pairs): 
      * vmId - ID of the VM to start the service on
      * all other parameters are directly inserted into marathon templates
* DELETE /api/services/$TENANT_ID/$SERVICE_NAME/
  * stops the specified service for the specified tenant

### Important Classes

#### de.hofuniversity.iisys.schub.openstack.api.RestService

Http Servlet serving the REST API, often generating output JSON when not covered by generic converters.

#### de.hofuniversity.iisys.schub.openstack.api.WrapperService

Singleton Wrapper service holding all existing tenants and their VMs, offering configuration objects and simplified access to further functionality such as adding Tenants, VMs and services. It loads persisted tenant and VM configurations from disk and creates the initial handlers and processors. Also persists changed tenant and VM configurations to disk.

#### de.hofuniversity.iisys.schub.openstack.config.*

Persistent configuration objects for the wrapper, the cluster configuration, tenant, VM and service configurations.

#### de.hofuniversity.iisys.schub.openstack.config.gen.ConfigGenerator

Creates initial configurations for Tenants, virtual machines and services, filling in general configuration data based on the cluster configuration and fixed values. Also generates all IDs for Tenants and VMs.

#### de.hofuniversity.iisys.schub.openstack.model.*

Classes defining the actual non-persistent state of Tenants and VMs, referencing their configuration.

#### de.hofuniversity.iisys.schub.openstack.monitoring.TenantMonitor

Monitor polling and setting the state of Tenants and VMs in the background, started by WrapperService.

#### de.hofuniversity.iisys.schub.openstack.processing.LdapHandler

Creates and deletes LDAP trees for tenants, filling in LDAP configuration information for all services.

Triggered by TenantProcessor.

#### de.hofuniversity.iisys.schub.openstack.processing.MailHandler

TODO: will create and destory mail contexts and fill in all mail-related configuration once available.

Triggered by TenantProcessor.

#### de.hofuniversity.iisys.schub.openstack.processing.ServiceHandler

Creates or deletes services in Marathon and offers to check the status of all services for a tenant. Sets the services' default or initial configuration using their specialized configurators upon tenant initialization.

Triggered either via WrapperService or by TenantProcessor when VMs are created and have a non-empty service array.

#### de.hofuniversity.iisys.schub.openstack.processing.SqlHanlder

Creates and deletes SQL databases for tenants, filling in database configuration information for all services.

Triggered by TenantProcessor.

#### de.hofuniversity.iisys.schub.openstack.processing.TenantProcessor

Main utility initializing tenants and their VMs that should later on also handle updating of a tenant's or a VM's configuration. It triggers most other handlers managing different backend systems and VMs.

Triggered by WrapperSerivce.

#### de.hofuniversity.iisys.schub.openstack.processing.VMHandler

Handles the creation and deletion of Tenant VMs, giving them their initial configuration and copying the Tenant's SSL certificate and key onto them.

Triggered by TenantProcessor.

#### de.hofuniversity.iisys.schub.openstack.services.*

Configuration routines for each service setting a working default configuration. Default values are either generated for the tenant or read from schub-services.properties.

Triggered by ServiceHandler.

#### de.hofuniversity.iisys.schub.openstack.util.ConfigLoader

Loads the wrapper's and cluster's properties from openstack-wrapper.properties.

#### de.hofuniversity.iisys.schub.openstack.util.ConfigStorage

Wrapper Storing tenant and VM configuration data to disk.

#### de.hofuniversity.iisys.schub.openstack.util.ServiceConstants

Collection of all service-related constands, mainly consisting of their respective template variables.

#### de.hofuniversity.iisys.schub.openstack.util.TenantConfigConverter

Utility converting tenant and VM configurations from and to JSON.