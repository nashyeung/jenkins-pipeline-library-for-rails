#!/usr/bin/groovy

@NonCPS
def call(allSpecs, testSplits, splitCount) {
  // 1. Get the list of all specs and list of specs run in last build
  allSpecs = allSpecs.collect { it.toString() }
  def lastBuildSpecs = []

  // 2. Normalize file names in testSplits, get the full list of specs run last time
  for (int i = 0; i < testSplits.size; i++) {
    testSplits[i] = testSplits[i].collect {
      it.replaceAll(/(java|class)$/, 'rb').replaceAll(/\/html\/haml_spec/, '.html.haml_spec')
    }.findAll {
      it.endsWith('spec.rb')
    }.unique()

    lastBuildSpecs += testSplits[i]
  }
  lastBuildSpecs = lastBuildSpecs.unique().findAll { allSpecs.contains(it) }

  // 3. Split specs newly added this time evenly
  def remainingSpecs = allSpecs - lastBuildSpecs
  Collections.shuffle(remainingSpecs.toList())
  remainingSpecs = remainingSpecs.collate(remainingSpecs.size.intdiv(splitCount))
  if (remainingSpecs.size == splitCount + 1) {
    remainingSpecs[0] += remainingSpecs[splitCount]
  }

  // 4. Calculate the list of specs for each worker
  def results = [:]
  for (int i = 0; i < splitCount; i++) {
    results[i] = [] + lastBuildSpecs - testSplits[i] + remainingSpecs[i]
    results[i].removeAll([null])
  }

  return results
}
