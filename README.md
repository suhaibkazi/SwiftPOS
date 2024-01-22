A SwiftSku Project

Simply keeps up.

SwiftSKU Assessment

Doubts/ Clarity Required
1. Screen size could be adaptive, as I’m building for an Android phone/tablet. 
    1. Also it’s understandable if we are assuming that a specific or custom device would be provided for wherever this POS software would be used.
    2. I’m going to build keeping in mind the specified constraints size (1920*1080) landscape.
2. I’m going to assume the cart section isn’t collapsible as it is fixed to the left of the screen/activity
3. The CTAs look like tabs instead of proper buttons, so I’ve decided to slightly modify the design as required and possibly display the hierarchy in the importance of each button.
4. I’m going to take some liberty in changing a few aspects of the app and making it easier for the user in terms of UX.
5. There’s a slight concern in the discount scenario as it's mentioned “Store A (The discount percentage is 10 % and the amount is 25 %)” with the amount also postfixed with a % instead of a $ symbol. I’m going to assume it’s just a typo. 
6. The tax scenarios lack complete information concerning the implementation. So I’m going to assume some attributes and implement them in a way that allows for further modifications.
7. I’m going to implement this using 
    - [✓] Architecture -> MVVM
    - [✓] DI -> Dagger HILT
    - [✓] DB -> Room DB
    - [✓] UI -> Compose
    - [✓] Observer -> Flows
