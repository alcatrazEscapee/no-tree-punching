#  Part of the No Tree Punching mod by AlcatrazEscapee.
#  Work under copyright. See the project LICENSE.md for details.

from typing import Sequence
from mcresources import ResourceManager, utils, loot_tables


def generate(rm: ResourceManager):
    # Loose rocks
    rm.crafting_shaped('cobblestone_from_rocks', ('XX', 'XX'), {'X': 'notreepunching:stone_loose_rock'}, 'minecraft:cobblestone').with_advancement('notreepunching:stone_loose_rock')
    for stone in ('andesite', 'diorite', 'granite'):
        rm.crafting_shaped('%s_cobblestone_from_rocks' % stone, ('XX', 'XX'), {'X': 'notreepunching:%s_loose_rock' % stone}, 'notreepunching:%s_cobblestone' % stone).with_advancement('notreepunching:%s_loose_rock' % stone)
    for stone in ('sandstone', 'red_sandstone'):
        rm.crafting_shaped('%s_from_rocks' % stone, ('XX', 'XX'), {'X': 'notreepunching:%s_loose_rock' % stone}, 'minecraft:%s' % stone).with_advancement('notreepunching:%s_loose_rock' % stone)

    # Stairs, Slabs, Walls
    for stone in ('andesite', 'diorite', 'granite'):
        cobble = 'notreepunching:%s_cobblestone' % stone

        # crafting recipes
        rm.crafting_shaped('%s_cobblestone_stairs' % stone, ('X  ', 'XX ', 'XXX'), {'X': cobble}, (4, cobble + '_stairs')).with_advancement(cobble)
        rm.crafting_shaped('%s_cobblestone_slab' % stone, ('XXX',), {'X': cobble}, (6, cobble + '_slab')).with_advancement(cobble)
        rm.crafting_shaped('%s_cobblestone_wall' % stone, ('XXX', 'XXX'), {'X': cobble}, (6, cobble + '_wall')).with_advancement(cobble)

        # stone cutting
        rm.recipe(('stonecutting', '%s_cobblestone_stairs' % stone), 'minecraft:stonecutting', {
            'ingredient': utils.ingredient(cobble),
            'result': cobble + '_stairs',
            'count': 1
        }).with_advancement(cobble)
        rm.recipe(('stonecutting', '%s_cobblestone_slab' % stone), 'minecraft:stonecutting', {
            'ingredient': utils.ingredient(cobble),
            'result': cobble + '_slab',
            'count': 2
        })
        rm.recipe(('stonecutting', '%s_cobblestone_wall' % stone), 'minecraft:stonecutting', {
            'ingredient': utils.ingredient(cobble),
            'result': cobble + '_wall',
            'count': 1
        })

        # smelting
        rm.recipe(('smelting', '%s_from_cobblestone' % stone), 'minecraft:smelting', {
            'ingredient': utils.ingredient(cobble),
            'result': 'minecraft:%s' % stone,
            'experience': 0.1,
            'cookingtime': 200
        })

    # Clay tool
    rm.crafting_shapeless('clay_brick_from_balls', ('notreepunching:clay_tool', 'minecraft:clay_ball'), 'notreepunching:clay_brick').with_advancement('minecraft:clay_ball')
    rm.crafting_shapeless('clay_brick_from_blocks', ('notreepunching:clay_tool', 'minecraft:clay'), (4, 'notreepunching:clay_brick')).with_advancement('minecraft:clay')

    # Misc
    rm.crafting_shapeless('plant_string', ['notreepunching:plant_fiber'] * 3, 'notreepunching:plant_string').with_advancement('notreepunching:plant_fiber')
    rm.recipe(('smelting', 'string_from_plant_string'), 'minecraft:smelting', {
        'ingredient': utils.ingredient('notreepunching:plant_string'),
        'result': 'minecraft:string',
        'experience': 0.1,
        'cookingtime': 200
    })
    rm.crafting_shapeless('flint_from_gravel', ['minecraft:gravel'] * 3, (2, 'minecraft:flint')).with_advancement('minecraft:gravel')

    # Wood Planks
    for wood in ('acacia', 'oak', 'dark_oak', 'jungle', 'birch', 'spruce', 'crimson', 'warped'):
        if wood == 'crimson' or wood == 'warped':
            name = '%s_stem' % wood
        else:
            name = '%s_log' % wood
        tool_damaging_shaped(rm, '%s_planks_with_saw' % wood, ('S', 'W'), {'S': 'tag!notreepunching:saws', 'W': 'tag!minecraft:%ss' % name}, (4, 'minecraft:%s_planks' % wood)).with_advancement('minecraft:%s' % name)
        tool_damaging_shaped(rm, '%s_planks_with_flint_axe' % wood, ('S', 'W'), {'S': 'tag!notreepunching:weak_saws', 'W': 'tag!minecraft:%ss' % name}, (2, 'minecraft:%s_planks' % wood)).with_advancement('minecraft:%s' % name)

    # Sticks
    tool_damaging_shaped(rm, 'sticks_from_logs_with_saw', ('SW',), {'S': 'tag!notreepunching:saws', 'W': 'tag!minecraft:logs'}, (8, 'minecraft:stick')).with_advancement('tag!minecraft:logs')
    tool_damaging_shaped(rm, 'sticks_from_planks_with_saw', ('SW',), {'S': 'tag!notreepunching:saws', 'W': 'tag!minecraft:planks'}, (2, 'minecraft:stick')).with_advancement('tag!minecraft:planks')

    tool_damaging_shaped(rm, 'sticks_from_logs_with_flint_axe', ('SW',), {'S': 'tag!notreepunching:weak_saws', 'W': 'tag!minecraft:logs'}, (6, 'minecraft:stick')).with_advancement('tag!minecraft:logs')
    tool_damaging_shaped(rm, 'sticks_from_planks_with_flint_axe', ('SW',), {'S': 'tag!notreepunching:weak_saws', 'W': 'tag!minecraft:planks'}, (1, 'minecraft:stick')).with_advancement('tag!minecraft:planks')

    # Tools
    for tool in ('iron', 'gold', 'diamond'):
        ingot = 'tag!forge:ingots/%s' % tool if tool != 'diamond' else 'tag!forge:gems/diamond'
        rm.crafting_shaped('%s_knife' % tool, ('I', 'S'), {'S': 'tag!forge:rods/wooden', 'I': ingot}, 'notreepunching:%s_knife' % tool).with_advancement(ingot)
        rm.crafting_shaped('%s_mattock' % tool, ('III', ' SI', ' S '), {'S': 'tag!forge:rods/wooden', 'I': ingot}, 'notreepunching:%s_mattock' % tool).with_advancement(ingot)
        rm.crafting_shaped('%s_saw' % tool, ('  S', ' SI', 'SI '), {'S': 'tag!forge:rods/wooden', 'I': ingot}, 'notreepunching:%s_saw' % tool).with_advancement(ingot)

    for tool in ('knife', 'mattock', 'saw'):
        rm.recipe('netherite_%s' % tool, 'minecraft:smithing', {
            'base': utils.item_stack('notreepunching:diamond_%s' % tool),
            'addition': utils.item_stack('minecraft:netherite_ingot'),
            'result': utils.item_stack('notreepunching:netherite_%s' % tool)
        }).with_advancement('minecraft:netherite_ingot')

    # Flint Tools
    rm.crafting_shaped('flint_axe', ('PI', 'S '), {'S': 'tag!forge:rods/wooden', 'I': 'notreepunching:flint_shard', 'P': 'tag!forge:string'}, 'notreepunching:flint_axe').with_advancement('notreepunching:flint_shard')
    rm.crafting_shaped('flint_hoe', ('PII', 'S  ', 'S  '), {'S': 'tag!forge:rods/wooden', 'I': 'notreepunching:flint_shard', 'P': 'tag!forge:string'}, 'notreepunching:flint_hoe').with_advancement('notreepunching:flint_shard')
    rm.crafting_shaped('flint_knife', ('I', 'S'), {'S': 'tag!forge:rods/wooden', 'I': 'notreepunching:flint_shard'}, 'notreepunching:flint_knife').with_advancement('notreepunching:flint_shard')
    rm.crafting_shaped('flint_pickaxe', ('IPI', 'ISI', ' S '), {'S': 'tag!forge:rods/wooden', 'I': 'notreepunching:flint_shard', 'P': 'tag!forge:string'}, 'notreepunching:flint_pickaxe').with_advancement('notreepunching:flint_shard')
    rm.crafting_shaped('flint_shovel', (' II', ' PI', 'S  '), {'S': 'tag!forge:rods/wooden', 'I': 'notreepunching:flint_shard', 'P': 'tag!forge:string'}, 'notreepunching:flint_shovel').with_advancement('notreepunching:flint_shard')
    rm.crafting_shaped('macuahuitl', (' IS', 'ISI', 'SI '), {'S': 'tag!forge:rods/wooden', 'I': 'notreepunching:flint_shard'}, 'notreepunching:macuahuitl').with_advancement('notreepunching:flint_shard')

    # Misc Tools
    rm.crafting_shaped('clay_tool', ('  I', ' II', 'I  '), {'I': 'tag!forge:rods/wooden'}, 'notreepunching:clay_tool').with_advancement('tag!forge:rods/wooden')
    rm.crafting_shaped('fire_starter', ('SP', 'FS'), {'S': 'tag!forge:rods/wooden', 'P': 'tag!forge:string', 'F': 'notreepunching:flint_shard'}, 'notreepunching:fire_starter').with_advancement('notreepunching:flint_shard')

    # Pottery firing
    for pottery in ('large_vessel', 'small_vessel', 'bucket', 'flower_pot', 'brick'):
        clay = 'notreepunching:clay_' + pottery
        if pottery == 'flower_pot':
            fired = 'minecraft:flower_pot'
        elif pottery == 'brick':
            fired = 'minecraft:brick'
        else:
            fired = 'notreepunching:ceramic_' + pottery
        rm.recipe(('smelting', pottery), 'minecraft:smelting', {
            'ingredient': utils.ingredient(clay),
            'result': fired,
            'experience': 0.1,
            'cookingtime': 200
        })
        rm.recipe(('campfire', pottery), 'minecraft:campfire_cooking', {
            'ingredient': utils.ingredient(clay),
            'result': fired,
            'experience': 0.1,
            'cookingtime': 600
        })

    # Knife crafting
    knife = 'tag!notreepunching:knives'
    rm.crafting_shapeless('string_from_wool_with_knife', ('tag!minecraft:wool', knife), (4, 'minecraft:string')).with_advancement('tag!minecraft:wool')
    rm.crafting_shapeless('string_from_web_with_knife', ('minecraft:cobweb', knife), (8, 'minecraft:string')).with_advancement('minecraft:cobweb')
    rm.crafting_shapeless('plant_fiber_from_sugarcane_with_knife', ('minecraft:sugar_cane', knife), (3, 'notreepunching:plant_fiber')).with_advancement('minecraft:sugar_cane')
    rm.crafting_shapeless('plant_fiber_from_wheat_with_knife', ('minecraft:wheat', knife), (2, 'notreepunching:plant_fiber')).with_advancement('minecraft:wheat')
    rm.crafting_shapeless('plant_fiber_from_vines_with_knife', ('minecraft:vine', knife), (5, 'notreepunching:plant_fiber')).with_advancement('minecraft:vine')
    rm.crafting_shapeless('plant_fiber_from_cactus_with_knife', ('minecraft:cactus', knife), (3, 'notreepunching:plant_fiber')).with_advancement('minecraft:cactus')
    rm.crafting_shapeless('plant_fiber_from_leaves_with_knife', ('tag!minecraft:leaves', knife), 'notreepunching:plant_fiber').with_advancement('tag!minecraft:leaves')
    rm.crafting_shapeless('plant_fiber_from_saplings_with_knife', ('tag!minecraft:saplings', knife), (2, 'notreepunching:plant_fiber')).with_advancement('tag!minecraft:saplings')
    rm.crafting_shapeless('plant_fiber_from_small_flowers_with_knife', ('tag!minecraft:small_flowers', knife), 'notreepunching:plant_fiber').with_advancement('tag!minecraft:small_flowers')
    rm.crafting_shapeless('plant_fiber_from_tall_flowers_with_knife', ('tag!minecraft:tall_flowers', knife), (2, 'notreepunching:plant_fiber')).with_advancement('tag!minecraft:tall_flowers')

    rm.crafting_shapeless('leather_from_boots_with_knife', ('minecraft:leather_boots', knife), (3, 'minecraft:leather')).with_advancement('minecraft:leather_boots')
    rm.crafting_shapeless('leather_from_leggings_with_knife', ('minecraft:leather_leggings', knife), (6, 'minecraft:leather')).with_advancement('minecraft:leather_leggings')
    rm.crafting_shapeless('leather_from_chestplate_with_knife', ('minecraft:leather_chestplate', knife), (7, 'minecraft:leather')).with_advancement('minecraft:leather_chestplate')
    rm.crafting_shapeless('leather_from_helmet_with_knife', ('minecraft:leather_helmet', knife), (4, 'minecraft:leather')).with_advancement('minecraft:leather_helmet')

    rm.crafting_shapeless('melon_slices_with_knife', ('minecraft:melon', knife), (9, 'minecraft:melon_slice')).with_advancement('minecraft:melon')

    # Compat: BYG
    # Replace log -> plank recipes, and add weak saw variants
    condition = mod_loaded('byg')
    for wood in ('aspen', 'baobab', 'blue_enchanted', 'cherry', 'cika', 'cypress', 'ebony', 'ether', 'fir', 'green_enchanted', 'holly', 'jacaranda', 'lament', 'mahogany', 'mangrove', 'maple', 'nightshade', 'palm', 'pine', 'rainbow_eucalyptus', 'redwood', 'skyris', 'willow', 'witch_hazel', 'zelkova'):
        rm.crafting_shaped('byg:%s_planks' % wood, ('S', 'W'), {'S': 'tag!notreepunching:saws', 'W': 'tag!byg:%s_logs' % wood}, (4, 'byg:%s_planks' % wood), conditions=condition)
        rm.crafting_shaped('compat/byg_%s_planks_with_flint_axe' % wood, ('S', 'W'), {'S': 'tag!notreepunching:weak_saws', 'W': 'tag!byg:%s_logs' % wood}, (2, 'byg:%s_planks' % wood), conditions=condition)

    # Compat: Quark
    # Remove recipes that add alternate stone tool recipes
    condition = mod_loaded('quark')
    for tool in ('sword', 'shovel', 'pickaxe', 'axe', 'hoe'):
        remove_recipe(rm, 'quark:tweaks/crafting/utility/tools/stone_%s' % tool, conditions=condition)

    # Compat: Farmers Delight
    # Replace a recipe which uses the wooden shovel with one using a stick instead
    condition = mod_loaded('farmersdelight')
    rm.crafting_shaped('farmersdelight:cooking_pot', ('bSb', 'iWi', 'iii'), {'b': 'minecraft:brick', 'i': 'minecraft:iron_ingot', 'S': 'minecraft:stick', 'W': 'minecraft:water_bucket'}, 'farmersdelight:cooking_pot', conditions=condition)


def mod_loaded(mod_id: str):
    return {
        'type': 'forge:mod_loaded',
        'modid': mod_id
    }


def tool_damaging_shaped(rm: ResourceManager, name_parts: utils.ResourceIdentifier, pattern: Sequence[str], ingredients: utils.Json, result: utils.Json, group: str = None, conditions: utils.Json = None):
    return rm.recipe(name_parts, 'notreepunching:tool_damaging', {
        'recipe': {
            'type': 'minecraft:crafting_shaped',
            'group': group,
            'pattern': pattern,
            'key': utils.item_stack_dict(ingredients, ''.join(pattern)[0]),
            'result': utils.item_stack(result),
            'conditions': utils.recipe_condition(conditions)
        }
    })


def remove_recipe(rm: ResourceManager, name_parts: utils.ResourceIdentifier, conditions: utils.Json = None):
    rm.recipe(name_parts, 'forge:conditional', {'recipes': []}, conditions=conditions)


def generate_vanilla(rm: ResourceManager):
    # Remove wood crafting recipes
    for wood in ('acacia', 'oak', 'dark_oak', 'jungle', 'birch', 'spruce', 'crimson', 'warped'):
        remove_recipe(rm, '%s_planks' % wood)

    remove_recipe(rm, 'stick')

    # Remove wood and stone tools
    for tool in ('pickaxe', 'shovel', 'hoe', 'sword', 'axe'):
        remove_recipe(rm, 'wooden_%s' % tool)
        remove_recipe(rm, 'stone_%s' % tool)

    remove_recipe(rm, 'campfire')
    remove_recipe(rm, 'flower_pot')
    remove_recipe(rm, 'brick')

    # Add optional plant fiber to loot tables
    rm.block_loot('grass', [{
        'entries': {
            'type': 'minecraft:alternatives',
            'children': utils.loot_entry_list([{
                'name': 'minecraft:grass',
                'conditions': loot_tables.match_tool('minecraft:shears')
            }, {
                'name': 'notreepunching:plant_fiber',
                'conditions': [
                    loot_tables.match_tool('tag!notreepunching:knives'),
                    loot_tables.random_chance(0.25)
                ]
            }, {
                'conditions': loot_tables.random_chance(0.125),
                'functions': [
                    loot_tables.fortune_bonus(2),
                    'minecraft:explosion_decay'
                ],
                'name': 'minecraft:wheat_seeds'
            }])
        }, 'conditions': None
    }])
    rm.block_loot('tall_grass', [{
        'entries': {
            'type': 'minecraft:alternatives',
            'children': utils.loot_entry_list([{
                'name': 'minecraft:grass',
                'conditions': loot_tables.match_tool('minecraft:shears')
            }, {
                'name': 'notreepunching:plant_fiber',
                'conditions': [
                    loot_tables.match_tool('tag!notreepunching:knives'),
                    loot_tables.random_chance(0.25)
                ]
            }, {
                'name': 'minecraft:wheat_seeds',
                'conditions': [
                    'minecraft:survives_explosion',
                    {
                        'condition': 'minecraft:block_state_property',
                        'block': 'minecraft:tall_grass',
                        'properties': {'half': 'lower'}
                    },
                    loot_tables.random_chance(0.125)
                ]
            }])
        }
    }])
