# WebTW

This is a little project that implements the most important features of [Taskwarrior](https://taskwarrior.org/) for me.  
I try to follow the "Getting Things Done" principle, so I need some features to be used on my mobile and on all devices i use frequently.  

These are:  

* Contexted tasks
* Create new task
* Modify/define due, wait, project, estimation, tags, annotations
* Set task done

## Installation

Clone it: [WebTW Repository](https://github.com/ulrichschinz/webtw)

## Build and start jar

To build the project run:  

```bash
± clojure -T:build ci
```

This will create a jar-file in the `target/` directory. Then you can use it:  

```bash
± java -jar target/webtw-0.1.0-SNAPSHOT.jar
```

By default the app will start listening on port 4040.

## Configuration

To protect the app from being used by anyone basic-auth is used.  
Currently it is defaulted to user `bau` and password `bap`.  
You can configure it by providing user and pass as environment variables:  

```bash
± export BASIC_AUTH_USER="hans"
± export BASIC_AUTH_PASS="dampf"
± java -jar target/webtw-0.1.0-SNAPSHOT.jar
INFO: Starting httpkit server on port: 4040
```

## Build docker image

```bash
λ ± docker build -t webtw .
```

### docker-compose example

FIXME: TODO

## Run repl

For developmen purpose I use a repl and some configuration in my vim.  
Hint: Clojure has to be installed to get it running ;).  
To start the repel you can do the following:  

```bash
± clojure -M:repl/rebel
nREPL server started on port 32869 on host localhost - nrepl://localhost:32869
[Rebel readline] Type :repl/help for online help info
user=> (require 'de.schinz.webtw)
nil
user=> (in-ns 'de.schinz.webtw)
#namespace[de.schinz.webtw]
de.schinz.webtw=> (-main)
INFO: Starting httpkit server on port: 4040
#function[clojure.lang.AFunction/1]
de.schinz.webtw=>
```

Here we go, tha app is started and you can start developing... Have fun!

## License

Copyright © 2023 Ulrich Schinz

Distributed under the Eclipse Public License version 1.0. See [LICENSE](https://github.com/ulrichschinz/webtw/blob/main/LICENSE)
