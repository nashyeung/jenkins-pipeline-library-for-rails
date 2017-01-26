#!/usr/bin/groovy

def bundle(envs, bundleArgs='', needUnstashGemfile=false) {
  if (needUnstashGemfile) {
    unstash 'Gemfile'
  }
  withEnv(envs) { sh "bundle install ${bundleArgs}"  }
}

