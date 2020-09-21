//
// Created by Ray Eldath on 2020/9/6.
//

#include <iostream>
#include <memory>
#include <vector>

class Ray final {
private:
    std::string m_self;
public:
    explicit Ray(std::string self) : m_self{std::move(self)} {}

    void mua(const std::string &name) &{
        std::cout << name << ", mua!   —— " << m_self << std::endl;
    }

    void mua(const std::string &name) &&{
        std::cout << name << " is a bad guy :-(  —— " << m_self << std::endl;
    }
};

Ray makeDeadRay() {
    return Ray("A Dead Ray Eldath");
}

int main() {
    auto ray = std::make_unique<Ray>("Ray Eldath");
    ray->mua("Somebody");
    auto v = std::vector<int>();

    makeDeadRay().mua("Someone");
}