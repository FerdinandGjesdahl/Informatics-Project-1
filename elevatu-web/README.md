## Web-application
For release 3 we decided to create web application for the project. This is implemented through the use of Node and ReactJS, and therefore have some requirements to be met.

## Running the web project
### Requirements
The following requirements must be met to run web application elevatu-web:
> NODE v18.14.0 or above
> NPM  9.3.1 or above
Install all NPM packets in project, after navigating to elevatu-web, using: 
> NPM install

## How to run the project
> Navigate to elevatu-web folder in terminal

After pulling new project files, remember to install local requirements using the command:
> NPM install

To start application use the command: 
> NPM start 

If you wish to call upon a mock REST controller that
returns dummy data, navigate to
> src/helpers/DataSource.js

and set the `useMockData` variable to `true`.

## Testing
Using unit testing, and local react library as well as jest-dom which is installed.
> npm i --save-dev @testing-library/jest-dom

# Installs that are run during development (not necessary installs to run project!):
Installed protobuf compiler found on https://github.com/protocolbuffers/protobuf/releases.
Then ran code compiler code, to create js version of proto object, so we could connect
frontend with backend using js. Used command:
> protoc --js_out=import_style=commonjs,binary:. elevatu_objects.proto 

Installed to run code for js system
> npm install -g protoc-gen-js 

Installed to googles runtime for protobuf.
> npm install google-protobuf

Other NPM packets that are used:
NPM packages to install
> NPM install react-router-dom@6 (version 6)
> NPM install react-icons --save
> NPM install bootstrap
> NPM install axios