//
// Created by Ray Eldath on 2020/9/13.
//

#include "pimpl.cpp"
#include <memory>

int main() {
    auto w = std::make_unique<Widget>();
    w->hello();
    return 0;
}