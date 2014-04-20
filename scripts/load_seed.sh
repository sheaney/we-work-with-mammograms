#!/bin/bash

cd ${0%/*}
export PGPASSWORD=${WWWM_PGSQL_PASSWORD}
psql -d wwwm -U ${WWWM_PGSQL_USER} -h localhost -c '\i seed.sql'
