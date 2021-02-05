#  Part of the No Tree Punching mod by AlcatrazEscapee.
#  Work under copyright. See the project LICENSE.md for details.

from mcresources import ResourceManager


def generate(rm: ResourceManager):
    # First
    rm.lang({
        'itemGroup.notreepunching.items': 'No Tree Punching',
        'notreepunching.tooltip.small_vessel_more': '%d More...',
        'notreepunching.tile_entity.large_vessel': 'Large Vessel'
    })

    # Stone
    for stone in ('granite', 'andesite', 'diorite'):
        block = rm.blockstate('%s_cobblestone' % stone)
        block.with_block_model()
        block.with_item_model()
        block.with_tag('cobblestone')
        rm.item_tag('cobblestone', '%s_cobblestone' % stone)  # both block and item tag
        block.with_block_loot('notreepunching:%s_cobblestone' % stone)
        block.with_lang(lang('%s cobblestone', stone))
        block.make_stairs()
        block.make_slab()
        block.make_wall()
        for piece in ('stairs', 'slab', 'wall'):
            block = rm.block('%s_cobblestone_%s' % (stone, piece))
            block.with_lang(lang('%s cobblestone %s', stone, piece))
            block.with_tag('minecraft:' + piece + ('s' if not piece.endswith('s') else ''))  # plural tag

    for stone in ('granite', 'andesite', 'diorite', 'stone', 'sandstone', 'red_sandstone'):
        block = rm.blockstate('%s_loose_rock' % stone)
        block.with_block_model(textures='minecraft:block/%s' % stone, parent='notreepunching:block/loose_rock')
        block.with_block_loot('notreepunching:%s_loose_rock' % stone)
        block.with_lang(lang('%s loose rock', stone))

        # flat item model for the block item
        item = rm.item_model('%s_loose_rock' % stone)
        item.with_tag('loose_rocks')  # item tag is needed for recipes

    # Pottery
    for pottery in ('worked', 'large_vessel', 'small_vessel', 'bucket', 'flower_pot'):
        block = rm.blockstate('clay_%s' % pottery)
        block.with_block_model(textures='minecraft:block/clay', parent='notreepunching:block/pottery_%s' % pottery)
        block.with_item_model()
        block.with_block_loot('notreepunching:clay_%s' % pottery)
        if pottery == 'worked':
            block.with_lang(lang('worked clay'))
        else:
            block.with_lang(lang('clay %s', pottery))

    block = rm.blockstate('ceramic_large_vessel')
    block.with_block_model(textures='notreepunching:block/ceramic', parent='notreepunching:block/pottery_large_vessel')
    block.with_item_model()
    block.with_block_loot({
        'entries': {
            'name': 'notreepunching:ceramic_large_vessel',
            'functions': [
                {
                    'function': 'minecraft:copy_name',
                    'source': 'block_entity'
                }, {
                    'function': 'minecraft:copy_nbt',
                    'source': 'block_entity',
                    'ops': [{
                        'source': '',
                        'target': 'BlockEntityTag',
                        'op': 'replace'
                    }]
                }
            ],
        }
    })
    block.with_lang(lang('ceramic large vessel'))

    # Tools
    for tool in ('iron', 'gold', 'diamond'):
        item = rm.item_model('%s_mattock' % tool, parent='item/handheld')
        item.with_lang(lang('%s mattock', tool))

        item = rm.item_model('%s_saw' % tool, parent='item/handheld')
        item.with_lang(lang('%s saw', tool))
        item.with_tag('saws')

        item = rm.item_model('%s_knife' % tool, parent='item/handheld')
        item.with_lang(lang('%s knife', tool))
        item.with_tag('knives')

    # Flint
    for tool in ('axe', 'pickaxe', 'shovel', 'hoe', 'knife'):
        item = rm.item_model('flint_%s' % tool, parent='item/handheld')
        item.with_lang(lang('flint %s', tool))

    item = rm.item_model('macuahuitl', parent='item/handheld')
    item.with_lang(lang('macuahuitl'))

    rm.item('flint_knife').with_tag('knives')
    rm.item('flint_axe').with_tag('weak_saws')

    for item_name in ('flint_shard', 'plant_fiber', 'plant_string', 'clay_brick', 'ceramic_small_vessel', 'clay_tool', 'fire_starter'):
        item = rm.item_model(item_name)
        item.with_lang(lang(item_name))

    # ceramic bucket, since it uses a very custom model
    rm.item('ceramic_bucket').with_lang(lang('ceramic bucket'))
    rm.data(('models', 'item', 'ceramic_bucket'), {
        'parent': 'forge:item/default',
        'textures': {
            'base': 'notreepunching:item/ceramic_bucket',
            'fluid': 'forge:item/mask/bucket_fluid_drip'
        },
        'loader': 'forge:bucket',
        'fluid': 'empty'
    }, root_domain='assets')

    # Misc Tags
    rm.item('plant_string').with_tag('forge:string')
    rm.block('minecraft:gravel').with_tag('always_breakable').with_tag('always_drops')

    rm.block_tag('always_breakable', '#minecraft:leaves', 'minecraft:gravel', '#forge:dirt', 'minecraft:grass', 'minecraft:podzol', 'minecraft:coarse_dirt', '#minecraft:sand')
    rm.block_tag('always_drops', '#minecraft:leaves', 'minecraft:gravel', '#forge:dirt', 'minecraft:grass', 'minecraft:podzol', 'minecraft:coarse_dirt', '#minecraft:sand')

    rm.item_tag('fire_starter_logs', '#minecraft:logs', '#minecraft:planks')
    rm.item_tag('fire_starter_kindling', '#forge:rods/wooden', '#minecraft:saplings', '#minecraft:leaves', '#forge:string', 'notreepunching:plant_fiber')

    # Add cobblestone to existing similar tags
    rm.item_tag('minecraft:stone_tool_materials', '#notreepunching:cobblestone')
    rm.item_tag('minecraft:stone_crafting_materials', '#notreepunching:cobblestone')
    rm.block_tag('forge:cobblestone', '#notreepunching:cobblestone')
    rm.item_tag('forge:cobblestone', '#notreepunching:cobblestone')

    rm.item('ceramic_small_vessel').with_tag('large_vessel_blacklist').with_tag('small_vessel_blacklist')
    rm.item('ceramic_large_vessel').with_tag('large_vessel_blacklist').with_tag('small_vessel_blacklist')
    rm.item('minecraft:shulker_box').with_tag('large_vessel_blacklist').with_tag('small_vessel_blacklist')
    for color in ('white', 'orange', 'magenta', 'light_blue', 'yellow', 'lime', 'pink', 'gray', 'light_gray', 'cyan', 'purple', 'blue', 'brown', 'green', 'red', 'black'):
        rm.item('minecraft:%s_shulker_box' % color).with_tag('large_vessel_blacklist').with_tag('small_vessel_blacklist')


def generate_vanilla(rm: ResourceManager):
    # Vanilla Tags
    rm.item('flint').with_tag('notreepunching:flint_knappable')
    for block in ('grass_block', 'dirt', 'coarse_dirt', 'gravel', 'sand', 'red_sand', 'terracotta', 'stone', 'andesite', 'diorite', 'granite', 'sandstone', 'red_sandstone', 'podzol'):
        rm.block(block).with_tag('notreepunching:loose_rock_placeable_on')


def lang(key: str, *args) -> str:
    return ((key % args) if len(args) > 0 else key).replace('_', ' ').replace('/', ' ').title()
