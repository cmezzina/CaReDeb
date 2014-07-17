
.PHONY: parser debugger clean
parser: parser/parser.jj
	cd parser;	rm *.java *.class 2>/dev/null;	javacc parser.jj

debugger: parser
	javac Debugger.java

clean:
	find . -name "*.class" -exec rm {} \;	

	
all: parser debugger