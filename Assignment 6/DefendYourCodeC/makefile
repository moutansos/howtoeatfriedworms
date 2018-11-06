dyc: main.o
	gcc -g -o dyc -pedantic -Wall -Wextra -Werror main.o -lpcre2-8 -lssl -lcrypto -lb64

main.o: main.c main.h
	gcc -g -pedantic -Wall -Wextra -Werror -c main.c

clean:
	rm dyc main.o