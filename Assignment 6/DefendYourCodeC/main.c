
#define PCRE2_CODE_UNIT_WIDTH 8

//Contributors: Travis Currier, Daylyn Hoxie, Ben Brougher
#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <limits.h>
#include <stdbool.h>
#include <pcre2.h>
#include <unistd.h>
#include <string.h>
#include <openssl/evp.h>
#include <openssl/sha.h>
#include <openssl/crypto.h>
#include <openssl/bio.h>
#include <openssl/buffer.h>
#include <linux/random.h>

#include "main.h"

static const char FIRST_NAME_PROMPT[] = "Please input your first name \n(Valid characters are upper and lower case letters and hyphens. Limited to 1 to 50 characters, more will be truncated):";
static const char LAST_NAME_PROMPT[] = "Please input your last name \n(Valid characters are upper and lower case letters and hyphens. Limited to 1 to 50 characters, more will be truncated):";
static const char VAL1_PROMPT[] = "Please input the first number. It must be from –2147483647 to 2147483647";
static const char VAL2_PROMPT[] = "Please input the second number. It must be from –2147483647 to 2147483647";
static const char READ_FILE_PROMPT[] = "Please input the name of an existing .txt file in the current directory. The name must be less than 50 characters and more than 1";
static const char WRITE_FILE_PROMPT[] = "Please input a name of a non-existing .txt file to write to in the current directory. The name must be less than 50 characters and more than 1";
static const char FIRST_PASSWORD_PROMPT[] = "Please input a password of the following format. It may contain, 1-255 characters, A-Z upper or lower case, 0-9, and special characters(-_+=!@#$%%^&*()): ";
static const char SECOND_PASSWORD_PROMPT[] = "Please input the password again: ";

static const char NAME_REGEX[] = "^[A-Za-z]{1,50}$";
static const char FILE_REGEX[] = "^[\\w\\- ]+\\.[Tt][Xx][Tt]$";
static const char PASS_REGEX[] = "^[\\w\\d\\-_\\+=!@#$%%^&\\*\\(\\)]{1,255}$";
static const char INTEGER_REGEX[] = "^\\-?\\d+$";

static const bool READ_FILE_MUST_EXIST = true;
static const bool WRITE_FILE_MUST_EXIST = false;

int main() {
    srand(time(0));

    char firstName[51];
    promptForValidText(firstName, sizeof(firstName)/sizeof(firstName[0]) - 1, FIRST_NAME_PROMPT, NAME_REGEX);

    char lastName[51];
    promptForValidText(lastName, sizeof(lastName)/sizeof(lastName[0]) - 1, LAST_NAME_PROMPT, NAME_REGEX);

    long long int val1 = promptForValidInteger(VAL1_PROMPT);

    long long int val2 = promptForValidInteger(VAL2_PROMPT);

    char readFileName[51];
    promptForValidFileName(readFileName, sizeof(readFileName)/sizeof(readFileName[0]) - 1, READ_FILE_PROMPT, FILE_REGEX, READ_FILE_MUST_EXIST);
    
    char writeFileName[51];
    promptForValidFileName(writeFileName, sizeof(writeFileName)/sizeof(writeFileName[0]) - 1, WRITE_FILE_PROMPT, FILE_REGEX, WRITE_FILE_MUST_EXIST);
    
    char firstEnteredPassword[256];
    memset(firstEnteredPassword, 0, sizeof(firstEnteredPassword));
    promptForValidText(firstEnteredPassword, sizeof(firstEnteredPassword)/sizeof(firstEnteredPassword[0]) - 1, FIRST_PASSWORD_PROMPT, PASS_REGEX);

    storePassword(firstEnteredPassword);

    char secondEnteredPassword[256];
    memset(secondEnteredPassword, 0, sizeof(secondEnteredPassword));
    promptForValidText(secondEnteredPassword, sizeof(secondEnteredPassword)/sizeof(secondEnteredPassword[0]) - 1, SECOND_PASSWORD_PROMPT, PASS_REGEX);
    
    comparePasswords(secondEnteredPassword, sizeof(secondEnteredPassword)/sizeof(secondEnteredPassword[0]) - 1);
    
    outputInfoToFile(firstName, lastName, val1, val2, readFileName, writeFileName);
    
    return 0;
}

void storePassword(char * firstPassword) {
    FILE * storeInFile = fopen("password_storage.txt", "w+");

    char passwordSalt[51];
    generateSalt(passwordSalt, sizeof(passwordSalt)/sizeof(passwordSalt[0]) - 1);
    passwordSalt[50] = '\0';

    uint32_t outputBytes = 32;
    char hexResult[2*outputBytes+1];
    memset(hexResult, 0, sizeof(hexResult));
    uint8_t binResult[outputBytes];
    memset(hexResult, 0, sizeof(binResult));
    PBKDF2_HMAC_SHA_512(firstPassword, strlen(firstPassword), (unsigned char*)passwordSalt, 50, 500, outputBytes, hexResult, binResult);

    fprintf(storeInFile, "%s\n", passwordSalt);
    fprintf(storeInFile, "%s\n", hexResult);
    fclose(storeInFile);
}

void comparePasswords(char * secondPassword, size_t passwordBufferSize) {
    bool equal = false;
    while(equal == false){
        FILE * takeFromStorage = fopen("password_storage.txt", "r");
	    char passwordSalt[51];
	    fscanf(takeFromStorage, "%50[^\n]\n", passwordSalt);
	    passwordSalt[50] = '\0';
	    
	    uint32_t outputBytes = 32;
	    char hexResult[2*outputBytes+1];
	    memset(hexResult, 0, sizeof(hexResult));
	    uint8_t binResult[outputBytes];
	    memset(hexResult, 0, sizeof(binResult));
	    PBKDF2_HMAC_SHA_512(secondPassword, strlen(secondPassword), (unsigned 	char*)passwordSalt, 50, 500, outputBytes, hexResult, binResult);

	    char passwordFromFile[2*outputBytes+1];
	    fscanf(takeFromStorage, "%64[^\n]\n", passwordFromFile);

	    if(strcmp(hexResult, passwordFromFile) != 0) {
            printf("Invalid password. Try again!");  
		    promptForValidText(secondPassword, passwordBufferSize, SECOND_PASSWORD_PROMPT, PASS_REGEX);
	    } else {
	    	printf("The passwords match!\n");
	    	equal = true;
	    }
        fclose(takeFromStorage);
    }
}
            
void outputInfoToFile(char * firstName, char * lastName, const long long int val1, const long long int val2, char * readFileName, char * writeFileName){
    FILE * fileToWrite = fopen(writeFileName,"w");
    FILE * fileToRead = fopen(readFileName, "r");
    
    long long int addVals = val1 + val2;
    long long int multVals = val1 * val2;
    
    fprintf(fileToWrite, "%s %s\n", firstName, lastName);
    fprintf(fileToWrite, "%lld + %lld = %lld\n", val1, val2, addVals);
    fprintf(fileToWrite, "%lld * %lld = %lld\n", val1, val2, multVals);
    
    char c;
    while((c = getc(fileToRead)) != EOF) {
        fprintf(fileToWrite, "%c", c);
    }
    fclose(fileToWrite);
    fclose(fileToRead);  
}
            
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

void promptForValidText(char buff[], const size_t bufferSize, const char promptText[], const char regexString[]) {
    bool isValidInput = false;
    PCRE2_SPTR regexPattern = (PCRE2_SPTR)regexString;
    int errorNumber;
    size_t errorOffset;
    do {
        printf("%s\n", promptText);
        fgets(buff, bufferSize, stdin);
        clearBuffer(buff);
        buff[strcspn(buff, "\n")] = 0; //Removes trailing newline
        PCRE2_SPTR input = (PCRE2_SPTR)buff;
        pcre2_code *result = pcre2_compile(regexPattern, PCRE2_ZERO_TERMINATED, PCRE2_MULTILINE, &errorNumber, &errorOffset, NULL);

        if(result == NULL) {
            PCRE2_UCHAR buffer[256];
            pcre2_get_error_message(errorNumber, buffer, sizeof(buffer));
            printf("PCRE2 compilation failed at offset %zu: %s\n", errorOffset, buffer);
            continue;
        }
        pcre2_match_data *matchData = pcre2_match_data_create_from_pattern(result, NULL);
        int matchResult = pcre2_match(result, input, strnlen(buff, bufferSize), 0, 0, matchData, NULL);
        if (matchResult < 0) {
            switch(matchResult) {
                case PCRE2_ERROR_NOMATCH: 
                    printf("Invalid input. "); 
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
            printf("Error. Please retry file input. ");
        } else {
            isValidInput = true;
        }
    } while(!isValidInput);
}


//https://ubuntuforums.org/archive/index.php/t-1059917.html
void clearBuffer(char *input)
{
    char *i;

    /* i is the pointer to the occurence of '\n'. */
    i = strchr(input, '\n');

    /* If there is no '\n' get the remaining input left in stdin, so it 
    isn't taken as input later on */
    if(i == NULL)
    while(getchar() != '\n');

    /* If there is a '\n' replace it with a '\0' because we later on don't 
    want the '\n' printed. */
    else
    *i = '\0';
}

//https://stackoverflow.com/questions/35381403/pbkdf2-implementation-in-openssl
void PBKDF2_HMAC_SHA_512(const char* pass,
                         const size_t passLength,
                         const unsigned char* salt,
                         const size_t saltLength, 
                         const int32_t iterations, 
                         const uint32_t outputBytes, 
                         char* hexResult, 
                         uint8_t* binResult) {
    unsigned int i;
    unsigned char digest[outputBytes];
    PKCS5_PBKDF2_HMAC(pass, passLength, salt, saltLength, iterations, EVP_sha512(), outputBytes, digest);
    for (i = 0; i < sizeof(digest); i++)
      {
        sprintf(hexResult + (i * 2), "%02x", 255 & digest[i]);
        binResult[i] = digest[i];
      };

}

//https://stackoverflow.com/questions/15767691/whats-the-c-library-function-to-generate-random-string
void generateSalt(char saltOutputBuff[], size_t bufferSize) { 
    char charset[] = "0123456789"
                     "abcdefghijklmnopqrstuvwxyz"
                     "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    while (bufferSize-- > 0) {
        size_t index = (double) rand() / RAND_MAX * (sizeof charset - 1);
        *saltOutputBuff++ = charset[index];
    }
    *saltOutputBuff = '\0';
}
