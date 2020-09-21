//
// Created by Ray Eldath on 2020/9/13.
//

#include "crtp_mixin.h"
#include <memory>

int main() {
    auto hello_world = std::make_unique<HelloWorld>("Ray", "Eldath");
    hello_world->hello();
    std::cout << std::endl;
    hello_world->world();

    return 0;
}