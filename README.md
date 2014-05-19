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

## Quick Setup

**Run tests**

`$ play test`

Please see here for more documention on [tests](https://github.com/feeeermendoza/we-work-with-mammograms/wiki/Testing#testing)

**Start the application in development mode:**

`$ play run`

**Run the application in production mode:**

`$ play start`

## Deploying

You can use the stage task to prepare your application to be run in place.
 The typical command for preparing a project to be run in place is:

`$ play stage`

To build a binary version of your application and deploy it to the server without any dependency on Play itself:

`$ play dist`

Please see [here](http://www.playframework.com/documentation/2.2.x/Production) for more details on running the application.
