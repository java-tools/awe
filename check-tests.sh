#!/bin/bash
errors=`cat awe-boot/target/surefire-reports/TEST-com.almis.awe.test.AllTestsSuiteIT.xml | grep 'errors\=\"[^\"]*' | grep 'failures\=\"[^\"]*' | sed -e 's/.*errors="//g' | sed -e 's/\"\s*skipped="[^\"]*"\s*failures="/\t/g' | sed -e 's/">//g' | awk 'BEGIN { FPAT="[0-9]+"} {printf ($1+$2>0)}'`
exit $errors