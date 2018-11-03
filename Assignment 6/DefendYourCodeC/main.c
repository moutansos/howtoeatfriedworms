
#define PCRE2_CODE_UNIT_WIDTH 8

#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <limits.h>
#include <stdbool.h>
#include <pcre2.h>
#include <unistd.h>
#include <string.h>
#include "main.h"

static const char FIRST_NAME_PROMPT[] = "Please input your first name \n(Valid characters are upper and lower case letters and hyphens. Limited to 1 to 50 characters, more will be truncated):";
static const char LAST_NAME_PROMPT[] = "Please input your last name \n(Valid characters are upper and lower case letters and hyphens. Limited to 1 to 50 characters):";
static const char VAL1_PROMPT[] = "Please input the first number. It must be from –2147483647 to 2147483647";
static const char VAL2_PROMPT[] = "Please input the second number. It must be from –2147483647 to 2147483647";
static const char READ_FILE_PROMPT[] = "Please input the name of an existing .txt file in the current directory. The name must be less than 50 charactes and more than 1";
static const char WRITE_FILE_PROMPT[] = "Please input a name of a non-existing .txt file to write to in the current directory. The name must be less than 50 characters and more than 1";
static const char FIRST_PASSWORD_PROMPT[] = "Please input a password of the following format. It may contain, 1-255 characters, A-Z upper or lower case, 0-9, and special characters(-_+=!@#$%%^&*()): ";
static const char SECOND_PASSWORD_PROMPT[] = "Please input the password again: ";

static const char NAME_REGEX[] = "^[A-Za-z]{1,50}";
static const char FILE_REGEX[] = "^[\\w\\- ]+\\.[Tt][Xx][Tt]";
static const char PASS_REGEX[] = "^[\\w\\d\\-_\\+=!@#$%%^&\\*\\(\\)]{1,255}";
static const char INTEGER_REGEX[] = "^\\d+";

static const bool READ_FILE_MUST_EXIST = true;
static const bool WRITE_FILE_MUST_EXIST = false;

int main() {
    char firstName[51];
    promptForValidText(firstName, sizeof(firstName)/sizeof(firstName[0]) - 1, FIRST_NAME_PROMPT, NAME_REGEX);

    char lastName[51];
    promptForValidText(lastName, sizeof(lastName)/sizeof(lastName[0]) - 1, LAST_NAME_PROMPT, NAME_REGEX);

    int val1 = promptForValidInteger(VAL1_PROMPT);

    int val2 = promptForValidInteger(VAL2_PROMPT);

    printf("\n Value 1: %d Value 2: %d \n", val1, val2); //TODO: Comment out when done with integer input

    char readFileName[51];
    promptForValidFileName(readFileName, sizeof(readFileName)/sizeof(readFileName[0]) - 1, READ_FILE_PROMPT, FILE_REGEX, READ_FILE_MUST_EXIST);
    
    char writeFileName[51];
    promptForValidFileName(writeFileName, sizeof(writeFileName)/sizeof(writeFileName[0]) - 1, WRITE_FILE_PROMPT, FILE_REGEX, WRITE_FILE_MUST_EXIST);
    
    char firstEnteredPassword[256];
    promptForValidText(firstEnteredPassword, sizeof(firstEnteredPassword)/sizeof(firstEnteredPassword[0]) - 1, FIRST_PASSWORD_PROMPT, PASS_REGEX);

    char secondEnteredPassword[256];
    promptForValidText(firstEnteredPassword, sizeof(secondEnteredPassword)/sizeof(secondEnteredPassword[0]) - 1, SECOND_PASSWORD_PROMPT, PASS_REGEX);
    
    //comparePasswords(secondEnteredPassword);
    
    //outputInfoToFile(firstName, lastName, val1, val2);
    
    return 0;
}

void storePassword(char * firstPassword){
    FILE * storeInFile = fopen("password_storage.txt", "w");
    fprintf(storeInFile, "%s", firstPassword);
    fclose(storeInFile);
}

/* void comparePasswords(char * secondPassword){

    FILE * takeFromStorage = fopen("password_storage.txt", "r");
    char * passwordOne = NULL;
    size_t passLen = 256;
    getLine(&passWordOne, &passLen, takeFromStorage);
    if(strcmp(secondPassWord, passwordOne) != 0){
        printf("Passwords do not match.\n");
    }
    else{
        printf("Passwords match.\n");
    }
    fclose(takeFromStorage);
} */
            
/* void outputInfoToFile(char * firstName, char * lastName, int val1, int val2){
    FILE * fileToWrite = fopen(writeFileName,"w");
    FILE * fileToRead = fopen(readFileName, "r");
    char * line = NULL;
    size_t lineLen = 0;
    
    long addVals = val1 + val2;
    long multVals = val1 * val2;
    
    fprintf(fileToWrite, "%s %s\n", firstName, lastName);
    fprintf(fileToWrite, "%d + %d = %d\n", val1, val2, addVals);
    fprintf(fileToWrite, "%d * %d = %d\n", val1, vall2, multVals);
    
    while(fscanf(fileToRead, %s, line) == 0){
        fprintf(fileToWrite, "%s\n", line);
    }
    fclose(fileToWrite);
    fclose(fileToRead);  
} */
            
long promptForValidInteger(const char promptText[]) {
    int inputIsValid = false;
    long inputValue;

    do {
        char inuptBuffer[20];
        promptForValidText(inuptBuffer, sizeof(inuptBuffer) / sizeof(inuptBuffer[0]) - 1, promptText, INTEGER_REGEX);
        inputValue = strtol(inuptBuffer, NULL, 10);
        if(inputValue <= INT_MAX && inputValue >= INT_MIN && errno == 0)
            inputIsValid = true;
    } while(!inputIsValid);

    return inputValue;
}

void promptForValidText(char buff[], const int bufferSize, const char promptText[], const char regexString[]) {
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
                    printf("Invalid input "); 
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
}

void promptForValidFileName(char buff[], const int bufferSize, const char promptText[], const char fileRegex[], const bool fileMustAlreadyExist) {
    bool isValidInput = false;
    do {
        promptForValidText(buff, bufferSize, promptText, fileRegex);

        if((fileMustAlreadyExist && access(buff, F_OK) == -1) || (!fileMustAlreadyExist && access(buff, F_OK) != -1)){
            printf("File does not exist in the current working directory. ");
        } else {
            isValidInput = true;
        }
    } while(!isValidInput);
}
