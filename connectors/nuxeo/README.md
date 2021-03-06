# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# ManifoldCF - Connectors - Nuxeo Connector

### Nuxeo repository connector

The repository connector has been developed using an incremental mode for seeding. Each time a job is executed is stored the current date and the request is done using the last seed version. Except if it is the first run, then all documents are crawled if it meet the specifications [1].

The metadata include with de document are: Uid, title, last modified, state, path, type, is checked out, repository, parent reference, description, language, coverage, valid, creators, contributors, last contributor, rights, expired, created, issued, nature, source, publisher, subjects and, optionally, attachments and tags. Specifically to note are included the notes and mime type.

##### **Configurations**
Required fields to connect are: protocol (http or https), host, port (default is 8080), path and if necessary a username and password.

##### **Specifications**
There are four possible specifications:
- Select domains to be crawled.
- Give the option of choosing if the tags must be processed.
- Select documents type to be crawled.
- Give the option of choosing if the attachments must be processed.

### Nuxeo authority connector

The authority connector has been developed using Acls [2]. Each document stores users and groups which have read permissions[3]. 

When a user searches then their username and groups are obtained and it are compared with the usernames and groups of the documents. In this way, they can only see the documents which contain his groups or his username.

##### **Configurations**
It is the same as for the repository connector.

[1] https://doc.nuxeo.com/display/NXDOC/REST+API  
[2] https://doc.nuxeo.com/display/NXDOC/ACLs  
[3] https://doc.nuxeo.com/display/NXDOC/Web+Adapters+for+the+REST+API