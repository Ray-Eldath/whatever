//
// Created by Ray Eldath on 2020/9/13.
//
#include <iostream>
#include <string>

class Hello {
private:
    std::string m_name;
public:
    explicit Hello(std::string name) : m_name{std::move(name)} {}

    void hello() const {
        std::cout << "hello from " << m_name << std::endl;
    }
};

template<typename T>
class World_Mixin {
private:
    std::string m_name;
public:
    explicit World_Mixin(std::string name) : m_name{std::move(name)} {}

    void world() const {
        static_cast<const T *>(this)->hello();
        std::cout << "world from " << m_name << std::endl;
    }
};

class HelloWorld : public Hello, public World_Mixin<HelloWorld> {
public:
    explicit HelloWorld() : HelloWorld("Ray", "Eldath") {}

    HelloWorld(std::string hello_name, std::string world_name) :
            Hello{std::move(hello_name)}, World_Mixin<HelloWorld>{std::move(world_name)} {}
};