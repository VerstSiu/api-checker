
# API Checker

## Usage

1. Check apis for different hosts:

    check-apis.json:

    ```json
    {
      "hosts": [
        "http://www.myhost.com/h1",
        "http://www.myhost.com/h2"
      ],
      "apis": [
        "api/1",
        "api/2"
      ]
    }
    ``` 

    check commands:
    
    ```shell
    java -jar api-checker.jar check-apis.json
    ```

2. Check apis for different alias:

    check-apis-alias.json:

    ```json
    {
      "hosts": [
        "http://www.myhost.com/h1"
      ],
      "apis_alias": [
        [
          "api/a1",
          "api/a2"
        ],
        [
          "api/b1",
          "api/b2"
        ]
      ]
    }
    ``` 

    check commands:
   
    ```shell
    java -jar api-checker.jar check-apis-alias.json
    ```

## License

```

   Copyright(c) 2019 VerstSiu

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

```