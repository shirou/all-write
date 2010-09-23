====================================================
all-write: Sample of Lucene on Google App Engine
====================================================

----------
Abstract
----------

All-write is an sample project of using Lucene Text-Search engine
library on Google App Engine.

This project use an N-gram analyzer in order to apply to Japanese
documents. 

To try this sample project, please go to this URL.

http://all-write.appspot.com

Then, enter test/test as email and pass. This web application is note
application. To create new document, press "NEW" and to post, press
"POST". You can search the document via left-side search box and
"search" button. Hit documents will be shown under the search box and
you can click a document to show and edit on the main text box.

Note: Since this does not have a exclusive control function, your
document may become broken or become something.

-----------------------------
How to Use the source code
-----------------------------

This is an sample project and have many problems. Please use with
carefully or just read source code.

NoteService
(http://github.com/shirou/all-write/blob/master/src/allwrite/service/NoteService.java)
or SearchService
(http://github.com/shirou/all-write/blob/master/src/allwrite/service/SearchService.java)
may help you.

~~~~~~~~~~~~~
Requirement
~~~~~~~~~~~~~

- lucene-3.0.2
- Google App engine SDK
- Eclipse and GAE/J plugin
- slim3 (http://sites.google.com/site/slim3appengine/)

~~~~~~~~~~~~~
How to try
~~~~~~~~~~~~~

- 1. Get slim3 blank zip file
- 2. unzip it 
- 3. Get Lucene jar files and set to war/WEB-INF/lib
-- lib/lucene-core
-- contrib/contrib/analyzers/common/lucene-analyzers-3.0.2
- 4. git clone this project
- 5. import directory to new project
- 6. deploy
- 7. enjoy!

Note: I have not try this direction, sorry. It can run, might be...

----------
Design
----------

~~~~~~~~~~~~~~~~~~~~~~~~
1. Index Store
~~~~~~~~~~~~~~~~~~~~~~~~

Lucene uses an Index file to text search. Popular Lucene application
uses a file (or files) as an index. However, Google App Engine does
not have an File System so can not store index into a file.

This project use RAMDirectory as an index. RAMDirectory is one of the
implementation of index. This uses an memory to store index and is
serializable. Google App Engine Datastore has a byte storage called
'blob', so I use blobs and RAMDirectory to store index.

~~~~~~~~~~~~~~~~~~~~~~~~
2. Entity Size Limit
~~~~~~~~~~~~~~~~~~~~~~~~

I use a blob to store index, but a entity that has blob can hold only
1MB. 

Fortunately, Lucene can handle multiple indexes using
MultiReader. MultiReader consists multiple IndexReader and each Index
Reader can read an index.

This project spread multiple index to multiple entity. When a search
query comes, collect entities from Datastore, create IndexReaders, and
then create MultiReader. MultiReader can search across these indexes
without any attention.

-----------------------------
Implementation Problems
-----------------------------

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
1. NullPointerException on IndexWriter/Reader
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Whenever create a IndexWriter, Lucene lock the RAMDirectory. But
invoked method Directory.makeLock() causes NullPointerexception.

To avoid this, set a LockFactory expressly like this ::

  RAMDirectory ramdir = new RAMDirectory();
  
  SingleInstanceLockFactory lockFac = new SingleInstanceLockFactory();
  ramIdx.setLockFactory(lockFac);

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
2. AccessControlException on IndexWriter.optimize()
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

IndexWriter.optimize() causes AccessControlException access denied
(java.lang.RuntimePermission modifyThreadGroup). IndexWriter use a
ConcurrentMergeScheduler as an default merge scheduler. Because that
use threads but Google App Store does not have a thread,
AccessControlException is caused.

To avoid this, use SerialMergeScheduler instead like this ::

  SerialMergeScheduler serialMerge = new SerialMergeScheduler();
  iWriter.setMergeScheduler(serialMerge);

------------
Problems
------------

~~~~~~~~~~~~~~~~~~~~~~
1. Search Performance
~~~~~~~~~~~~~~~~~~~~~~

I evaluated the search performance. and found the search it self is
very fast (10msec when 4000 documents) but collect indexes from
Datastore is too slow like 5000msec.

To reduce that time, I use these methods.

- 1. use batch get
- 2. reduce index size to reduce the number of entity
-- Lucene Document has only id 

Then reduce the time to 2000msec but still slow.

One of the reason is use N-gram analyzer. If documents is only
English, can use other analyzer and reduce index size. However, I am
native-Japanese and has to use N-gram (or with morphological
analysis).
