#!/usr/bin/env python
"""
Packager
Utility to morph a BlueJ project into a proper Java package.

Depends on txtrevise utility and Java compiler (javac) and Java archiver (jar).
"""
import sys
import re
import os
import argparse
import shutil
import glob

def package(package, mainClass, classPath, rootFolder, verbose):

	jars = path = ''
	jarsArray = []

	# Create packager root
	if not os.path.exists('packager'):
		os.mkdir('packager')

	if verbose: print('Finding required JAR libraries:')
	os.chdir('{0}/{1}'.format(rootFolder, classPath))
	for jar in glob.glob('*.jar'):
		if verbose: print(jar)
		jars += '{0}/{1} '.format(classPath, jar)
		jarsArray.append('../{0}/{1}/{2};'.format(rootFolder, classPath, jar))

	os.chdir('..')

	if verbose: print('Copying Java source files:')
	for java in glob.glob('*.java'):
		if verbose: print(java)
		shutil.copy(java, '../packager')

	os.chdir('../packager')

	if verbose: print('Writing manifest for {0}.{1}'.format(package, mainClass))
	f = open('Manifest.mf'.format(rootFolder), 'w')
	f.write('Main-Class: {0}.{1}\n'.format(package, mainClass))
	f.write('Class-Path: {0}\n'.format(jars))
	f.close()

	if verbose: print('Prepending package declaration to sources:')
	for java in glob.glob('*.java'):
		if verbose: print(java)
		os.system('txtrevise -q -f {0} -l 1 -m "//package" -r "{1}"'.format(java, 'package {0};'.format(package)))

	if verbose: print('Creating folder structure from package declaration:')
	structure = 'package {0}'.format(package)
	folders = re.split('\.|\s', structure)
	folders.pop(0)
	for folder in folders:
		path += "{0}/".format(folder)

	if verbose: print(path)
	os.makedirs(path)

	if verbose: print('Copy source files to package structure:')
	for java in glob.glob('*.java'):
		if verbose: print('Copy {0}'.format(java))
		shutil.copy(java, path)

	if verbose: print('Compiling classes:')
	cp = ".;"
	for jar in jarsArray:
		cp += jar

	for java in glob.glob('*.java'):
		if verbose: print('Compiling {0}'.format(java))
		os.system('javac -cp {0} {1}/{2}'.format(cp[:-1], path, java))

	#shutil.rmtree('../packager')

# Handle any command line arguments
parser = argparse.ArgumentParser(description='Utility to morph a BlueJ project into proper Java package.')
parser.add_argument('-p', '--package', action='store', dest='package', metavar="PACKAGE", required=True)
parser.add_argument('-m', '--mainClass', action='store', dest='mainClass', metavar="MAINCLASS", required=True)
parser.add_argument('-cp', '--classPath', action='store', dest='classPath', metavar="FOLDER", required=True)
parser.add_argument('-r', '--root', action='store', dest='rootFolder', metavar="ROOTFOLDER", required=True)
parser.add_argument('-v', '--verbose', action='store_true', dest='verbose')
argv = parser.parse_args()

package(argv.package, argv.mainClass, argv.classPath, argv.rootFolder, argv.verbose)
