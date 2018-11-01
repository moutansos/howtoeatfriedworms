
#define PCRE2_CODE_UNIT_WIDTH 8

#include <stdio.h>
#include <stdbool.h>
#include <pcre2.h>
#include "main.h"

static const char FIRST_NAME_PROMPT[] = "Please input your first name \n(Valid characters are upper and lower case letters and hyphens. Limited to 1 to 50 characters, more will be truncated):";
// static const char LAST_NAME_PROMPT[] = "Please input your last name \n(Valid characters are upper and lower case letters and hyphens. Limited to 1 to 50 characters): \n";
// static const char VAL1_PROMPT[] = "Please input the first number. It must be from –2147483647 to 2147483647\n";
// static const char VAL2_PROMPT[] = "Please input the second number. It must be from –2147483647 to 2147483647\n";
// static const char READ_FILE_PROMPT[] = "Please input the name of an existing .txt file in the current directory.\n";
// static const char WRITE_FILE_PROMPT[] = "Please input a name of a non-existing .txt file to write to in the current directory.\n";
// static const char FIRST_PASSWORD_PROMPT[] = "Please input a password of the following format. It may contain, 1-255 characters, A-Z upper or lower case, 0-9, and special characters(-_+=!@#$%%^&*()): \n";
// static const char SECOND_PASSWORD_PROMPT[] = "Please input the password again: \n";

static const char NAME_REGEX[] = "^[A-Za-z]{1,50}";
// static const char FILE_REGEX[] = "^[\\w\\- ]+\\.[Tt][Xx][Tt]$";
// static const char PASS_REGEX[] = "^[\\w\\d\\-_\\+=!@#$%%^&\\*\\(\\)]{1,255}$";

// static const bool READ_FILE_MUST_EXIST = true;
// static const bool WRITE_FILE_MUST_EXIST = false;

int main() {
    char firstName[52];
    promptForValidText(firstName, sizeof(firstName)/sizeof(char), FIRST_NAME_PROMPT, NAME_REGEX);


    return 0;
}

char* promptForValidText(char buff[], int bufferSize, const char promptText[], const char regexString[]) {
    bool isValidInput = false;
    PCRE2_SPTR regexPattern = (PCRE2_SPTR)regexString;
    int errorNumber;
    size_t errorOffset;
    do {
        printf("%s\n", promptText);
        fgets(buff, bufferSize, stdin);
        PCRE2_SPTR input = (PCRE2_SPTR)buff;
        pcre2_code *result = pcre2_compile(regexPattern, PCRE2_ZERO_TERMINATED, 0, &errorNumber, &errorOffset, NULL);

        if(result == NULL) {
            PCRE2_UCHAR buffer[256];
            pcre2_get_error_message(errorNumber, buffer, sizeof(buffer));
            printf("PCRE2 compilation failed at offset %zu: %s\n", errorOffset, buffer);
            continue;
        }
        pcre2_match_data *matchData = pcre2_match_data_create_from_pattern(result, NULL);
        int matchResult = pcre2_match(result, input, bufferSize, 0, 0, matchData, NULL);
        if (matchResult < 0) {
            switch(matchResult) {
                case PCRE2_ERROR_NOMATCH: 
                    printf("No match\n"); 
                    break;
                default: 
                    printf("Matching error %d\n", matchResult); 
                    break;
            }
        } else {
            isValidInput = true;
        }
        pcre2_match_data_free(matchData);
        pcre2_code_free(result);
    } while(!isValidInput);

    return buff;
}