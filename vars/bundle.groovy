#!/usr/bin/groovy

def call(envs, bundleArgs='', needUnstashGemfile=false) {
  if (needUnstashGemfile) {
    unstash 'Gemfile'
  }
  withEnv(envs) { sh "bundle install ${bundleArgs}"  }
}

