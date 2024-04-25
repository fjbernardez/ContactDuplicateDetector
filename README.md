# ContactDuplicateDetector

ContactDuplicateDetector takes a list of contacts and returns another list, indicating the level of duplication between
the contacts.

## Logic and Functionality

Data Loading
Contacts are loaded from a CSV file, avoiding duplicate entries during the initial load.

Duplicate Detection

The detection is based on three levels of probability: high, medium, and low.

    A high probability match is flagged when two contacts share the same email address.
    
    A medium probability is identified if contacts have the same address and their last names are similar within a certain threshold, determined by the Levenshtein distance.
    
    A low probability match is considered if contacts share the same zip code but have different emails, and their last names are slightly similar based on the Levenshtein distance.

Data Structures
The application uses a HashMap for quick lookup and comparison of contacts via email.
It maintains separate HashMap indices for addresses and zip codes, which store lists of contacts corresponding to each
address or zip code.

Avoiding Self-Comparison
To prevent a contact from being compared with itself, the system checks contact IDs before making comparisons.

## File Format and Location

### Input File Requirements

The application requires the input file to be in CSV (Comma-Separated Values) format, following a specific structure. Each record in the CSV should represent a contact with the following fields:

    Contact ID
    First Name
    Last Name
    Email
    Zip Code
    Address

The CSV file must be placed in the resources folder located at the root of the application directory.

### Output File Generation

After processing, the application generates an output CSV file with potential duplicate contacts and their associated probability levels. The output file will be saved in the resources folder with a name result.csv.
