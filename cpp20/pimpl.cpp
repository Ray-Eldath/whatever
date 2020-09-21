//
// Created by Ray Eldath on 2020/9/13.
//

#include "crtp_mixin.h"
#include <vector>
#include <string>
#include <memory>
#include <iostream>
#include "pimpl.h"

struct Widget::Impl {
    std::string name;
    std::vector<double> data;
    HelloWorld helloWorld;
};

void Widget::hello() {
    std::cout << "hello!" << std::endl;
    pImpl->helloWorld.world();
}

Widget::Widget() : pImpl(std::make_unique<Impl>()) {}

Widget::~Widget() = default;