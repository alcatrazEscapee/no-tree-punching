
from mcresources import ResourceManager, clean_generated_resources

import assets
import recipes


def main():
    rm = ResourceManager('notreepunching', '../src/main/resources')
    clean_generated_resources('../src/main/resources')

    assets.generate(rm)
    recipes.generate(rm)

    rm.flush()


if __name__ == '__main__':
    main()
