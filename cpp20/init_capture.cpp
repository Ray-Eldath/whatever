//
// Created by Ray Eldath on 2020/9/16.
//
#include <vector>
#include <string>
#include <iostream>
#include <memory>

class Ray {
public:
    const std::string m_name;

    explicit Ray(std::string name) : m_name{std::move(name)} {}
};

int main() {
    auto ray = std::make_unique<Ray>("Ray");
    std::cout << ray << std::endl;

    auto lambda = [data = std::move(ray)] { std::cout << data->m_name << std::endl; };
    lambda();
    std::cout << "done 1" << std::endl;

    std::cout << ray << std::endl;
    std::cout << ray->m_name << std::endl;
    std::cout << "done 2" << std::endl;

    return 0;
}