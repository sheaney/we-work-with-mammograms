## Requirements

1. Unix based system
2. [Java JRE 1.7](http://www.oracle.com/technetwork/java/javase/downloads/index.html?ssSourceSiteId=otnjp) or later
3. [Sass](http://sass-lang.com/install) (You might need to manually install [Ruby](https://www.ruby-lang.org/en/installation/) and [RubyGems](https://rubygems.org/pages/download) first if you do not already have it installed in your machine)
4. [PostgreSQL](http://www.postgresql.org/download/) installed and running with a database named ```wwwwm```

You will need to set up the following environment variables for establishing a database connection to PostgreSQL:

``WWWM_PGSQL_USER``,
``WWWM_PGSQL_PASSWORD``

Please refer [here](https://github.com/feeeermendoza/we-work-with-mammograms/wiki/Initializing-DB) for more information on setting up the database with seed data.

And the following environment variables for sending emails through the application:

``ADMIN_EMAIL``,
``ADMIN_EMAIL_PASS``

**These credentials are by default a gmail email account's credentials. However the application can be configured to use a different email server, see [here](https://github.com/feeeermendoza/we-work-with-mammograms/wiki/Emails)**

## Quick Setup

**Run tests**

`$ play test`

Please see here for more documention on [tests](https://github.com/feeeermendoza/we-work-with-mammograms/wiki/Testing#testing)

**Run application in development mode:**

`$ play run`

**Run application in production mode:**

`$ play start`

Then visit http://127.0.0.1:9000 in your browser.

## Deploying

You can use the stage task to prepare your application to be run in place.
 The typical command for preparing a project to be run in place is:

`$ play stage`

To build a binary version of your application and deploy it to the server without any dependency on Play itself:

`$ play dist`

Please see [here](http://www.playframework.com/documentation/2.2.x/Production) for more details on running the application.

## Uploading images to AWS S3

The application supports uploading and reading mammogram images from an existing Amazon S3 bucket. Please see [here](https://github.com/feeeermendoza/we-work-with-mammograms/wiki/Amazon-S3) for instructions on configuring the application to read/write images from S3
