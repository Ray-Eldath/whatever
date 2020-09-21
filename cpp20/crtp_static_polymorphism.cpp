//
// Created by Ray Eldath on 2020/9/11.
//
#include <memory>
#include <iostream>
#include <functional>

template<typename T>
class Base {
public:
    virtual ~Base() = default;

    std::string name() {
        return static_cast<T *>(this)->name();
    }

    void hello() { std::cout << "hello from " << name() << std::endl; }

private:
    explicit Base() {};
    friend T;
};

class Child1 : public Base<Child1> {
public:
    std::string name() { return "Ray Eldath"; }
};

class Child2 : public Base<Child2> {
public:
    std::string name() { return "YA Ray Eldath"; }
};

auto hook = [](auto *b) {
    std::cout << "instance " << b->name() << " deleted" << std::endl;
    delete b;
};

auto makeChild1() noexcept {
    std::unique_ptr<Child1, decltype(hook)> ptr(nullptr, hook);
    ptr.reset(new Child1());
    return ptr;
}

auto makeChild2() noexcept {
    std::unique_ptr<Child2, decltype(hook)> ptr(nullptr, hook);
    ptr.reset(new Child2());
    return ptr;
}

int main() {
    auto a = makeChild1();
    auto b = makeChild2();
    a->hello();
    b->hello();

    return 0;
}